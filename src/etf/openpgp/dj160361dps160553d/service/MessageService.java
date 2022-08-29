package etf.openpgp.dj160361dps160553d.service;


import org.bouncycastle.bcpg.*;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.bc.BcPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;


public class MessageService {

    public static void generatePGPMessage(PGPKeyPair senderKey, PGPPublicKey receiverKey, File inputFile, File outputFile, int symmetricAlgorithm, boolean toAuth, boolean toCompress, boolean toRadix64) throws IOException {
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

    public static void readPGPMessage (File msgIn, PGPSecretKeyRingCollection privateKeys, PGPPublicKeyRingCollection publicKeys) throws IOException, PGPException {
        InputStream in = new FileInputStream(msgIn);
        in = PGPUtil.getDecoderStream(new ByteArrayInputStream(in.readAllBytes()));

        PGPObjectFactory objFactory = new BcPGPObjectFactory(in);

        Object pgpObject = objFactory.nextObject();

        PGPEncryptedDataList cypherData;

        JPanel parent = new JPanel();

        System.out.println("hello");
        System.out.println(pgpObject.toString());

        if (pgpObject instanceof PGPEncryptedDataList) {
            System.out.println("decryption");
            cypherData = (PGPEncryptedDataList) pgpObject;
            PGPPrivateKey sessionKey = null;
            PGPPublicKeyEncryptedData publicKeyEncryptedData = null;
            Iterator<PGPEncryptedData> it = cypherData.getEncryptedDataObjects();
            String userID = "";


            while (sessionKey == null && it.hasNext()) {
                publicKeyEncryptedData = (PGPPublicKeyEncryptedData) it.next();
                PGPSecretKey fileSecretKey = privateKeys.getSecretKey(publicKeyEncryptedData.getKeyID());
                if (fileSecretKey != null) {
                    userID = fileSecretKey.getUserIDs().next();
                    String passPhrase = null;
                    JTextField passField = new JPasswordField();
                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Pass for user "+userID+" :"));
                    panel.add(passField);
                    int result = JOptionPane.showConfirmDialog(parent, panel, "Enter the password for user " + userID, JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                    while (result == JOptionPane.OK_OPTION) {
                        passPhrase = passField.getText();
                        if (!passPhrase.equals("") && passPhrase != null) break;
                    }
                    try {
                        sessionKey = fileSecretKey.extractPrivateKey(new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(passPhrase.toCharArray()));
                    }catch (PGPException e) {
                        JOptionPane.showMessageDialog(parent, "Invalid passphrase", "Decryption Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                }

            }
            if (sessionKey == null) {
                JOptionPane.showMessageDialog(parent, "No matching private key found for decryption", "Decryption Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            InputStream decryptedData = publicKeyEncryptedData.getDataStream(new BcPublicKeyDataDecryptorFactory(sessionKey));
            objFactory = new BcPGPObjectFactory(decryptedData);
            pgpObject = objFactory.nextObject();

        }
        if (pgpObject instanceof PGPCompressedData) {
            PGPCompressedData zipData = (PGPCompressedData) pgpObject;

            objFactory = new BcPGPObjectFactory(zipData.getDataStream());

            pgpObject = objFactory.nextObject();
            System.out.println("Data unzipped");
        }

        if (pgpObject instanceof PGPOnePassSignatureList sigHead) {

            PGPOnePassSignature ops = sigHead.get(0);

            PGPLiteralData literalData = (PGPLiteralData) objFactory.nextObject();

            InputStream dIn = literalData.getInputStream();

            int data;
            PGPPublicKey senderKey = publicKeys.getPublicKey(ops.getKeyID());

            if (senderKey == null) {
                JOptionPane.showMessageDialog(parent,
                        "Signature is signed by an untrusted party. No matching public key was found in the public key ring",
                        "Security warning",
                        JOptionPane.WARNING_MESSAGE);
            }


            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if(senderKey != null) ops.init(new BcPGPContentVerifierBuilderProvider(), senderKey);

            while ((data = dIn.read()) >= 0) {
                if(senderKey != null) ops.update((byte) data);
                out.write(data);
            }

            PGPSignatureList senderSignature = (PGPSignatureList) objFactory.nextObject();
            if(senderKey == null){
                JFileChooser fileChoose = new JFileChooser();
                fileChoose.setDialogTitle("Save message to...");
                int choice = fileChoose.showDialog(parent, "Save");
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = fileChoose.getSelectedFile();
                    file.createNewFile();
                    byte[] rawData = out.toByteArray();
                    OutputStream msgOut = new FileOutputStream(file);
                    msgOut.write(rawData);
                }
                out.close();
                return;
            }

            boolean verified = ops.verify(senderSignature.get(0));
            if (verified) {
                String userID = senderKey.getUserIDs().next();
                JOptionPane.showMessageDialog(parent,
                        "Message was signed by "+userID,
                        "Verification successful",
                        JOptionPane.INFORMATION_MESSAGE);
                System.out.println("signature verified.");
                JFileChooser fileChoose = new JFileChooser();
                fileChoose.setDialogTitle("Save message to...");
                int choice = fileChoose.showDialog(parent, "Save");
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = fileChoose.getSelectedFile();
                    file.createNewFile();
                    byte[] rawData = out.toByteArray();
                    OutputStream msgOut = new FileOutputStream(file);
                    msgOut.write(rawData);
                }
            } else {
                JOptionPane.showMessageDialog(parent,
                        "Signature couldn't be verified.",
                        "Security error",
                        JOptionPane.ERROR_MESSAGE);
            }
            out.close();

        } else if (pgpObject instanceof PGPLiteralData) {
            PGPLiteralData rawData = (PGPLiteralData) pgpObject;
            InputStream rawDataStream = rawData.getInputStream();
            JFileChooser fileChoose = new JFileChooser();
            fileChoose.setDialogTitle("Export message to...");
            int choice = fileChoose.showDialog(parent, "save");
            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChoose.getSelectedFile();
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int data;
                while ((data = rawDataStream.read()) >= 0) {
                    out.write(data);
                }
                out.close();
            }


        }


    }


}
