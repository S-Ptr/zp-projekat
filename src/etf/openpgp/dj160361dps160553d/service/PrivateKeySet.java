package etf.openpgp.dj160361dps160553d.service;

import etf.openpgp.dj160361dps160553d.InvalidPasswordException;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.PGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class PrivateKeySet {

    private static PGPSecretKeyRingCollection secretKeys;
    private static final PGPDigestCalculatorProvider hashCalculator = new BcPGPDigestCalculatorProvider();

    static {
        try {
            secretKeys = new PGPSecretKeyRingCollection(new ArrayList<>());
        } catch (IOException | PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public static PGPSecretKeyRingCollection getSecretKeys() {
        return secretKeys;
    }

    public static Object[][] getSecretKeysMatrix() {
        ArrayList<ArrayList<Object>> secretKeysArrayList = new ArrayList<>();
        secretKeys.getKeyRings().forEachRemaining(pgpSecretKeys -> {
            ArrayList<Object> objectArrayList = new ArrayList<>();
            String[] array = (new String(pgpSecretKeys.getPublicKeys().next().getRawUserIDs().next())).split(" ");
            objectArrayList.add(array[0]);
            objectArrayList.add(array[1].substring(1, array[1].length() - 1));
            objectArrayList.add(Long.toHexString(pgpSecretKeys.getPublicKeys().next().getKeyID()));
            secretKeysArrayList.add(objectArrayList);
        });
        Object[][] secretKeysArray = new Object[PrivateKeySet.getSecretKeys().size()][3];
        for (int i = 0; i < secretKeys.size(); i++) {
            secretKeysArray[i][0] = secretKeysArrayList.get(i).get(0);
            secretKeysArray[i][1] = secretKeysArrayList.get(i).get(1);
            secretKeysArray[i][2] = secretKeysArrayList.get(i).get(2);
        }
        return secretKeysArray;
    }

    public static ArrayList<String> getSecretKeysArray() {
        ArrayList<String> secretKeysArray = new ArrayList<>();
        secretKeys.getKeyRings().forEachRemaining(pgpSecretKeys -> {
            String[] array = (new String(pgpSecretKeys.getPublicKeys().next().getRawUserIDs().next())).split(" ");
            secretKeysArray.add(array[0] + " " + array[1].substring(1, array[1].length() - 1) + " " + Long.toHexString(pgpSecretKeys.getPublicKeys().next().getKeyID()));

        });
        return secretKeysArray;
    }

    public void addPrivateKey(PGPKeyPair keyPair, String name, String email, String passphrase) {
        try {
            PGPSecretKey key = new PGPSecretKey(PGPSignature.POSITIVE_CERTIFICATION, keyPair, name + " <" + email + ">",
                    hashCalculator.get(HashAlgorithmTags.SHA1),
                    null, null,
                    new BcPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                    new BcPBESecretKeyEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_128,
                            hashCalculator.get(HashAlgorithmTags.SHA1)).build(passphrase.toCharArray()));
            List<PGPSecretKey> keyRing = new ArrayList<>();
            keyRing.add(key);
            secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(secretKeys, new PGPSecretKeyRing(keyRing));
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrivateKey(long keyID, String passphrase) throws InvalidPasswordException {
        try {
            PGPSecretKeyRing keyRing = secretKeys.getSecretKeyRing(keyID);
            PGPKeyPair keyPair = keyRing.getSecretKey().extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
            if (keyPair.getKeyID() == keyID)
                secretKeys = PGPSecretKeyRingCollection.removeSecretKeyRing(secretKeys, secretKeys.getSecretKeyRing(keyID));
            else throw new InvalidPasswordException();
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrivateKey(String user, String passphrase) throws PGPException {
        Iterator<PGPSecretKeyRing> detectedUsers;
        detectedUsers = secretKeys.getKeyRings(user, true);//boolean means whether partial matches are allowed or not
        while (detectedUsers.hasNext()) {
            PGPSecretKeyRing current = detectedUsers.next();
            current.getSecretKey().extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
            secretKeys = PGPSecretKeyRingCollection.removeSecretKeyRing(secretKeys, detectedUsers.next());
        }
    }

    public PGPKeyPair getKeyPair(String user, String passphrase) throws PGPException {
        Iterator<PGPSecretKeyRing> matchingSecretKeys = secretKeys.getKeyRings(user, true);//partial matches allowed
        if (matchingSecretKeys.hasNext()) {
            PGPSecretKeyRing secretKeyRing = matchingSecretKeys.next();
            PGPSecretKey secretKey = secretKeyRing.getSecretKey();
            return secretKey.extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
        } else {
            System.out.println("No such secret key found: " + user);
        }
        return null;
    }


    public void exportPairToFile(String user) throws PGPException, IOException {
        JFrame parent = new JFrame();
        Iterator<PGPSecretKeyRing> matchingSecretKeys = secretKeys.getKeyRings(user, true);//partial matches allowed
        if (!matchingSecretKeys.hasNext()) {
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
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChoose.getSelectedFile();
            PGPSecretKeyRingCollection fileKeys = new PGPSecretKeyRingCollection(new ArmoredInputStream(new FileInputStream(file)), fingerprintCalc);
            for (PGPSecretKeyRing keyRing : fileKeys) {
                secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(secretKeys, keyRing);

            }
        }
    }

    public static void importKeyPairsFromFile(File file) throws IOException, PGPException {
        KeyFingerPrintCalculator fingerprintCalc = new BcKeyFingerprintCalculator();
        PGPSecretKeyRingCollection fileKeys = new PGPSecretKeyRingCollection(new ArmoredInputStream(new FileInputStream(file)), fingerprintCalc);
        for (PGPSecretKeyRing keyRing : fileKeys) {
            secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(secretKeys, keyRing);
        }
    }

    public PGPPublicKey getPublicKey(String user) throws PGPException {
        Iterator<PGPSecretKeyRing> matchingSecretKey = secretKeys.getKeyRings(user, true);//partial matches allowed
        if (matchingSecretKey.hasNext()) {
            return matchingSecretKey.next().getPublicKey();
        } else {
            System.out.println("No matching key");
            return null;
        }
    }

    public void exportPublicKey(String user) throws PGPException, IOException {
        Iterator<PGPSecretKeyRing> matchingSecretKeys = secretKeys.getKeyRings(user, true);//partial matches allowed
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
        } else {
            System.out.println("No matching secret key");
        }
    }

    public void exportPublicKey(String user, File file) throws IOException, PGPException {
        Iterator<PGPSecretKeyRing> matchingSecretKeys = secretKeys.getKeyRings(user, true);//partial matches allowed
        if (matchingSecretKeys.hasNext()) {
            PGPSecretKeyRing keyRing = matchingSecretKeys.next();
            OutputStream secretOut = new FileOutputStream(file.getAbsolutePath() + ".asc");
            secretOut = new ArmoredOutputStream(secretOut);
            keyRing.getPublicKey().encode(secretOut);
            secretOut.close();
        } else {
            System.out.println("No matching secret key");
        }
    }

}
