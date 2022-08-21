package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class PublicKeySet {

    public PGPPublicKeyRingCollection publicKeys;


    public PublicKeySet() throws PGPException, IOException {
        this.publicKeys = new PGPPublicKeyRingCollection(new ArrayList<PGPPublicKeyRing>());
    }

    public void importKeysFromFile() throws IOException, PGPException {
        KeyFingerPrintCalculator fingerprintCalc = new BcKeyFingerprintCalculator();
        JFrame parent = new JFrame();
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setDialogTitle("Import from...");
        int choice = fileChoose.showDialog(parent, "Import");
        if(choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChoose.getSelectedFile();
            PGPPublicKeyRingCollection fileKeys = new PGPPublicKeyRingCollection(new FileInputStream(file), fingerprintCalc);
            for(PGPPublicKeyRing keyRing : fileKeys) {
                this.publicKeys = PGPPublicKeyRingCollection.addPublicKeyRing(this.publicKeys, keyRing);
            }
        }

    }

    public void exportKeyToFile(String user) throws PGPException, IOException {
        JFrame parent = new JFrame();
        Iterator<PGPPublicKeyRing> matchingPublicKeys = this.publicKeys.getKeyRings(user, true);//partial matches allowed
        if(!matchingPublicKeys.hasNext()){
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
        }else{
            System.out.println("exportKeyToFile - No such public key found: "+user);
        }
    }

    public PGPPublicKey getPublicKey(String user) throws PGPException {
        Iterator<PGPPublicKeyRing> matchingPublicKey = this.publicKeys.getKeyRings(user, true);//partial matches allowed
        if(matchingPublicKey.hasNext()){
            return matchingPublicKey.next().getPublicKey();
        }else {
            System.out.println("getPublicKey - No such public key found: "+user);
            return null;
        }
    }

    public PGPPublicKeyRingCollection getPublicKeys(){
        return this.publicKeys;
    }
}
