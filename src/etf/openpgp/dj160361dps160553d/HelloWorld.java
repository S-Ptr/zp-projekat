package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;

import javax.crypto.Cipher;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        PrivateKeySet privateKeys;
        PublicKeySet publicKeys;
        MessageService msgService;
        try {
            privateKeys = new PrivateKeySet();
            msgService = new MessageService();
            publicKeys = new PublicKeySet();
        } catch (PGPException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        KeyService ks = new KeyService();
        JPanel parent = new JPanel();
        try {
            /*privateKeys.addPrivateKey(ks.generateKeyPair(KeyLength.RSA2048), "stefan", "stefan@stefex.com", "nijestefan");
            privateKeys.exportPairToFile("stefan@stefex.com");
            PGPKeyPair pgpKeyPair = privateKeys.getKeyPair("stefan@stefex.com", "nijestefan");
            if(pgpKeyPair != null) {System.out.println("gg");}*/

            /*privateKeys.addPrivateKey(ks.generateKeyPair(KeyLength.RSA2048), "azra", "azra@azco.com", "nijeazra");
            privateKeys.exportPairToFile("azra@azco.com");
            PGPPublicKey publicKey = privateKeys.getPublicKey("azra@azco.com");*/

            privateKeys.importKeyPairsFromFile(new File("testprivate.asc"));
            privateKeys.importKeyPairsFromFile(new File("testpublic.asc"));

            PGPKeyPair pgpKeyPair = privateKeys.getKeyPair("stefan@stefex.com", "nijestefan");
            PGPPublicKey publicKey = privateKeys.getPublicKey("azra@azco.com");
            //publicKeys.addPublicKeyRing(pgpKeyPair.getPublicKey());


            JFileChooser fileChoose = new JFileChooser();
            fileChoose.setDialogTitle("PGP moment");
            int choice = fileChoose.showDialog(parent, "Drzi palceve bajo");
            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChoose.getSelectedFile();
                File dest = new File("target.asc");
                dest.createNewFile();
                msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.AES_128,true,true,true); //works
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.AES_128,false,true,true); //works
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.AES_128,true,false,true);//works
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.AES_128,true,true,false); //works
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.TRIPLE_DES,true,true,true); //works
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.NULL,false,true,false); //works
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.NULL,true,true,false); //i think it works?
                //msgService.generatePGPMessage(pgpKeyPair, publicKey,file,dest, SymmetricKeyAlgorithmTags.NULL,true,false,false);//works
                //privateKeys.addPrivateKey(ks.generateKeyPair(KeyLength.RSA2048), "Stoka", "stoka@matijevic.com", "stoka1234");//vrlo sam smesan
                //msgService.generatePGPMessage(pgpKeyPair, ks.generateKeyPair(KeyLength.RSA2048).getPublicKey(),file,dest, SymmetricKeyAlgorithmTags.AES_128,true,true,true);//gg dosta mi je za danas, odoh na DMC5
                //System.out.println("we did it bro, now go check with kleopatra");
                msgService.readPGPMessage(dest,privateKeys.getSecretKeys(),publicKeys.getPublicKeys());
                System.out.println("i hope that's that");

            }
        } catch (PGPException e) {
            e.getUnderlyingException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
