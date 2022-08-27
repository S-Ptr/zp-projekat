package etf.openpgp.dj160361dps160553d;


import org.bouncycastle.bcpg.*;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;


public class MessageService {

    public void generatePGPMessage(PGPKeyPair senderKey, PGPPublicKey receiverKey, File inputFile, File outputFile, int symmetricAlgorithm, boolean toAuth, boolean toCompress, boolean toRadix64) throws IOException {
        PGPDataEncryptorBuilder encryptorBuilder;
        PGPContentSignerBuilder signerBuilder = new BcPGPContentSignerBuilder(senderKey.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1);
        PGPCompressedDataGenerator zipGenerator = null;
        PGPSignatureGenerator signatureGenerator;
        PGPEncryptedDataGenerator encryptor;
        byte[] writeData;

        File inFile = inputFile;
        OutputStream msgOut = new FileOutputStream(outputFile.getAbsolutePath());
        InputStream msgIn = new FileInputStream(inputFile.getAbsolutePath());
        //msgOut = new BufferedOutputStream(msgOut);

        OutputStream radixOut;
        OutputStream zipOut = null;
        OutputStream cryptOut;
        OutputStream finalOut = msgOut;
        ArrayList<OutputStream> outStreams = new ArrayList<OutputStream>();
        outStreams.add(msgOut);

        if(toAuth){
            File tempMsg = new File("tempMsg.asc");
            tempMsg.createNewFile();
            OutputStream tempOut = new FileOutputStream(tempMsg);
            signatureGenerator= new PGPSignatureGenerator(signerBuilder);
            try {
                signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, senderKey.getPrivateKey());
            } catch (PGPException e) {
                throw new RuntimeException(e);
            }
            Iterator<String> user = senderKey.getPublicKey().getUserIDs();
            if(user.hasNext()){
                PGPSignatureSubpacketGenerator sigHeaderGenerator = new PGPSignatureSubpacketGenerator();
                sigHeaderGenerator.addSignerUserID(false, user.next());
                signatureGenerator.setHashedSubpackets(sigHeaderGenerator.generate());
            }

            zipOut = tempOut;
            if(toCompress){
                zipGenerator = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
                zipOut = zipGenerator.open(tempOut);
            }

            try {
                signatureGenerator.generateOnePassVersion(false).encode(zipOut);
            } catch (PGPException e) {
                throw new RuntimeException(e);
            }

            OutputStream literalTempOut;
            
            literalTempOut = new PGPLiteralDataGenerator().open(zipOut, PGPLiteralDataGenerator.BINARY,inputFile);

            int data;
            while((data = msgIn.read()) != -1){
                literalTempOut.write(data);
                signatureGenerator.update((byte)data);

            }
            try {
                signatureGenerator.generate().encode(zipOut);
            } catch (PGPException e) {
                throw new RuntimeException(e);
            }
            literalTempOut.close();
            if(toCompress) zipOut.close();
            tempOut.close();

            inFile = new File("tempMsg.asc");
            msgIn = new FileInputStream(inFile);


        }

        writeData= msgIn.readAllBytes();

        if(toCompress && !toAuth){
            byte[] uncompressed = writeData;
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            zipGenerator = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
            zipOut = zipGenerator.open(array);
            OutputStream literalZipOut = new PGPLiteralDataGenerator().open(zipOut, PGPLiteralDataGenerator.BINARY,inFile);
            literalZipOut.write(uncompressed);
            literalZipOut.close();
            zipOut.close();
            writeData = array.toByteArray();
        }


        if(toRadix64){
            radixOut = new ArmoredOutputStream(outStreams.get(outStreams.size() - 1));
            outStreams.add(radixOut);
            finalOut = radixOut;
        }

        if(symmetricAlgorithm!=0){
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            encryptorBuilder = new BcPGPDataEncryptorBuilder(symmetricAlgorithm).setWithIntegrityPacket(true);
            encryptor = new PGPEncryptedDataGenerator(encryptorBuilder);
            encryptor.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(receiverKey).setSecureRandom(new SecureRandom()));
            try {
                cryptOut = encryptor.open(array, writeData.length);
                cryptOut.write(writeData);
                cryptOut.close();
                writeData = array.toByteArray();
            } catch (PGPException e) {
                throw new RuntimeException(e);
            }

        }
        //PGPUtil.writeFileToLiteralData(msgOut, PGPLiteralData.BINARY, (toAuth)?new File("tempMsg.asc"):inputFile, buffer);

        /*InputStream fileIn2 = new FileInputStream((toAuth)?new File("tempMsg.asc"):inputFile);
        int data;
        while((data = fileIn2.read()) != -1){
            finalOut.write(data);
            //System.out.println((char)data);

        }*/
        finalOut.write(writeData);
        finalOut.close();
        msgOut.close();
    }

    //public void readPGPMessage (File msgIn, File msgOut, PGPSecretKeyRingCollection privateKeys, PGPPublicKeyRingCollection publicKeys)


}
