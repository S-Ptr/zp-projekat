package etf.openpgp.dj160361dps160553d.service;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class PublicKeySet {

    public static PGPPublicKeyRingCollection publicKeys;

    static {
        try {
            publicKeys = new PGPPublicKeyRingCollection(new ArrayList<>());
        } catch (IOException | PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public static PGPPublicKeyRingCollection getPublicKeys() {
        return publicKeys;
    }

    public static Object[][] getPublicKeysMatrix() {
        ArrayList<ArrayList<Object>> publicKeysArrayList = new ArrayList<>();
        publicKeys.getKeyRings().forEachRemaining(pgpPublicKeys -> {
            ArrayList<Object> objectArrayList = new ArrayList<>();
            String[] array = (new String(pgpPublicKeys.getPublicKeys().next().getRawUserIDs().next())).split(" ");
            objectArrayList.add(array[0]);
            objectArrayList.add(array[1].substring(1, array[1].length() - 1));
            objectArrayList.add(Long.toHexString(pgpPublicKeys.getPublicKeys().next().getKeyID()));
            publicKeysArrayList.add(objectArrayList);
        });
        Object[][] publicKeysArray = new Object[PublicKeySet.getPublicKeys().size()][3];
        for (int i = 0; i < publicKeys.size(); i++) {
            publicKeysArray[i][0] = publicKeysArrayList.get(i).get(0);
            publicKeysArray[i][1] = publicKeysArrayList.get(i).get(1);
            publicKeysArray[i][2] = publicKeysArrayList.get(i).get(2);
        }
        return publicKeysArray;
    }

    public static void addPublicKeyRing(PGPPublicKey publicKey) {
        ArrayList<PGPPublicKey> list = new ArrayList<>();
        list.add(publicKey);
        publicKeys = PGPPublicKeyRingCollection.addPublicKeyRing(publicKeys, new PGPPublicKeyRing(list));
    }

    public void importKeysFromFile() throws IOException, PGPException {
        KeyFingerPrintCalculator fingerprintCalc = new BcKeyFingerprintCalculator();
        JFrame parent = new JFrame();
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setDialogTitle("Import from...");
        int choice = fileChoose.showDialog(parent, "Import");
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChoose.getSelectedFile();
            PGPPublicKeyRingCollection fileKeys = new PGPPublicKeyRingCollection(new ArmoredInputStream(new FileInputStream(file)), fingerprintCalc);
            for (PGPPublicKeyRing keyRing : fileKeys) {
                publicKeys = PGPPublicKeyRingCollection.addPublicKeyRing(publicKeys, keyRing);
            }
        }

    }

    public static void importKeysFromFile(File file) throws IOException, PGPException {
        KeyFingerPrintCalculator fingerprintCalc = new BcKeyFingerprintCalculator();
        PGPPublicKeyRingCollection fileKeys = new PGPPublicKeyRingCollection(new ArmoredInputStream(new FileInputStream(file)), fingerprintCalc);
        for (PGPPublicKeyRing keyRing : fileKeys) {
            publicKeys = PGPPublicKeyRingCollection.addPublicKeyRing(publicKeys, keyRing);
        }
    }

    public void exportKeyToFile(String user) throws PGPException, IOException {
        JFrame parent = new JFrame();
        Iterator<PGPPublicKeyRing> matchingPublicKeys = publicKeys.getKeyRings(user, true);//partial matches allowed
        if (!matchingPublicKeys.hasNext()) {
            throw new PGPException("No matching key found");
        }
        PGPPublicKeyRing publicKeyRing = matchingPublicKeys.next();
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setDialogTitle("Export to...");
        int choice = fileChoose.showDialog(parent, "Export");
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChoose.getSelectedFile();
            OutputStream secretOut = new FileOutputStream(file.getAbsolutePath() + ".asc");
            secretOut = new ArmoredOutputStream(secretOut);
            publicKeyRing.encode(secretOut);
            secretOut.close();
        } else {
            System.out.println("exportKeyToFile - No such public key found: " + user);
        }
    }

    public void exportKeyToFile(String user, File file) throws PGPException, IOException {
        Iterator<PGPPublicKeyRing> matchingPublicKeys = publicKeys.getKeyRings(user, true);//partial matches allowed
        if (!matchingPublicKeys.hasNext()) {
            throw new PGPException("No matching key found");
        }
        PGPPublicKeyRing publicKeyRing = matchingPublicKeys.next();
        OutputStream secretOut = new FileOutputStream(file.getAbsolutePath() + ".asc");
        secretOut = new ArmoredOutputStream(secretOut);
        publicKeyRing.encode(secretOut);
        secretOut.close();
    }

    public PGPPublicKey getPublicKey(String user) throws PGPException {
        Iterator<PGPPublicKeyRing> matchingPublicKey = publicKeys.getKeyRings(user, true);//partial matches allowed
        if (matchingPublicKey.hasNext()) {
            return matchingPublicKey.next().getPublicKey();
        } else {
            System.out.println("getPublicKey - No such public key found: " + user);
            return null;
        }
    }

    public PGPPublicKey getPublicKey(long keyID) {
        return this.getPublicKey(keyID);
    }
}
 
