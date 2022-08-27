package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.*;
import org.bouncycastle.openpgp.operator.bc.*;

import javax.print.attribute.HashAttributeSet;
import javax.swing.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class PrivateKeySet {

    private PGPSecretKeyRingCollection secretKeys;
    private PGPDigestCalculatorProvider hashCalculator;

    public PrivateKeySet() throws PGPException, IOException {
        this.secretKeys = new PGPSecretKeyRingCollection(new ArrayList<PGPSecretKeyRing>());
        this.hashCalculator = new BcPGPDigestCalculatorProvider();
    }

    public PGPSecretKey getSecretKey(long keyID) throws PGPException {
        return secretKeys.getSecretKey(keyID);
    }

    public void addPrivateKey (PGPKeyPair keyPair, String name, String email, String passphrase){
        try {
            PGPSecretKey key = new PGPSecretKey(PGPSignature.POSITIVE_CERTIFICATION, keyPair, name + " <" + email + ">",
                    hashCalculator.get(HashAlgorithmTags.SHA1),
                    null, null,
                    new BcPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                    new BcPBESecretKeyEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_128,
                            hashCalculator.get(HashAlgorithmTags.SHA1)).build(passphrase.toCharArray()));
            List<PGPSecretKey> keyRing = new ArrayList<>();
            keyRing.add(key);
            this.secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(this.secretKeys, new PGPSecretKeyRing(keyRing));
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrivateKey(long keyID, String passphrase) throws InvalidPasswordException {
        try {
            PGPSecretKeyRing keyRing = secretKeys.getSecretKeyRing(keyID);
            PGPKeyPair keyPair = keyRing.getSecretKey().extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
            if(keyPair.getKeyID()==keyID)
                this.secretKeys = PGPSecretKeyRingCollection.removeSecretKeyRing(this.secretKeys, this.secretKeys.getSecretKeyRing(keyID));
            else throw new InvalidPasswordException();
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrivateKey(String user, String passphrase) throws PGPException {
        Iterator<PGPSecretKeyRing> detectedUsers = null;
            detectedUsers = this.secretKeys.getKeyRings(user, true);//boolean means whether partial matches are allowed or not
            while(detectedUsers.hasNext()){
                PGPSecretKeyRing current = detectedUsers.next();
                current.getSecretKey().extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
                this.secretKeys = PGPSecretKeyRingCollection.removeSecretKeyRing(this.secretKeys, detectedUsers.next());
            }
    }

    public PGPKeyPair getKeyPair(String user, String passphrase) throws PGPException {
            Iterator<PGPSecretKeyRing> matchingSecretKeys = this.secretKeys.getKeyRings(user, true);//partial matches allowed
            if(matchingSecretKeys.hasNext()){
                PGPSecretKeyRing secretKeyRing = matchingSecretKeys.next();
                PGPSecretKey secretKey = secretKeyRing.getSecretKey();
                return secretKey.extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
            }else{
                System.out.println("No such secret key found: "+user);
            }
        return null;
    }


    public void exportPairToFile(String user) throws PGPException, IOException {
        JFrame parent = new JFrame();
        Iterator<PGPSecretKeyRing> matchingSecretKeys = this.secretKeys.getKeyRings(user, true);//partial matches allowed
        if(!matchingSecretKeys.hasNext()){
            throw new PGPException("No matching key found");
        }
        PGPSecretKeyRing secretKeyRing = matchingSecretKeys.next();
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setDialogTitle("Export to...");
        int choice = fileChoose.showDialog(parent, "Export");
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChoose.getSelectedFile();
                OutputStream secretOut = new FileOutputStream(file.getAbsolutePath() + ".asc");
                secretOut = new ArmoredOutputStream(secretOut);
                secretKeyRing.encode(secretOut);
                secretOut.close();
        }
    }

    public void importKeyPairsFromFile() throws IOException, PGPException {
        KeyFingerPrintCalculator fingerprintCalc = new BcKeyFingerprintCalculator();
        JFrame parent = new JFrame();
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setDialogTitle("Import from...");
        int choice = fileChoose.showDialog(parent, "Import");
        if(choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChoose.getSelectedFile();
            PGPSecretKeyRingCollection fileKeys = new PGPSecretKeyRingCollection(new ArmoredInputStream(new FileInputStream(file)), fingerprintCalc);
            for(PGPSecretKeyRing keyRing : fileKeys) {
                this.secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(this.secretKeys, keyRing);
            }
        }
    }

    public void importKeyPairsFromFile(File file) throws IOException, PGPException {
        KeyFingerPrintCalculator fingerprintCalc = new BcKeyFingerprintCalculator();
        PGPSecretKeyRingCollection fileKeys = new PGPSecretKeyRingCollection(new ArmoredInputStream(new FileInputStream(file)), fingerprintCalc);
        for (PGPSecretKeyRing keyRing : fileKeys) {
            this.secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(this.secretKeys, keyRing);
        }
    }

    public PGPPublicKey getPublicKey(String user) throws PGPException {
        Iterator<PGPSecretKeyRing> matchingSecretKey = this.secretKeys.getKeyRings(user, true);//partial matches allowed
        if(matchingSecretKey.hasNext()){
            return matchingSecretKey.next().getPublicKey();
        }else {
            System.out.println("No matching key");
            return null;
        }
    }

    public void exportPublicKey(String user) throws PGPException, IOException {
        Iterator<PGPSecretKeyRing> matchingSecretKeys = this.secretKeys.getKeyRings(user, true);//partial matches allowed
        JFrame parent = new JFrame();
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setDialogTitle("Export to...");
        if (matchingSecretKeys.hasNext()) {
            PGPSecretKeyRing keyRing = matchingSecretKeys.next();
            int choice = fileChoose.showDialog(parent, "Export");
            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChoose.getSelectedFile();
                OutputStream secretOut = new FileOutputStream(file.getAbsolutePath() + ".asc");
                secretOut = new ArmoredOutputStream(secretOut);
                keyRing.getPublicKey().encode(secretOut);
                secretOut.close();
            }
        }else{
            System.out.println("No matching secret key");
        }
    }

    public void exportPublicKey(String user, File file) throws IOException, PGPException {
        Iterator<PGPSecretKeyRing> matchingSecretKeys = this.secretKeys.getKeyRings(user, true);//partial matches allowed
        if (matchingSecretKeys.hasNext()) {
            PGPSecretKeyRing keyRing = matchingSecretKeys.next();
                OutputStream secretOut = new FileOutputStream(file.getAbsolutePath() + ".asc");
                secretOut = new ArmoredOutputStream(secretOut);
                keyRing.getPublicKey().encode(secretOut);
                secretOut.close();
        }else{
            System.out.println("No matching secret key");
        }
    }

    public PGPSecretKeyRingCollection getSecretKeys() {
        return this.secretKeys;
    }
}
