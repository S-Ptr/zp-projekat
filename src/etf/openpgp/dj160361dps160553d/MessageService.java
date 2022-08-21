package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.openpgp.PGPCanonicalizedDataGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;

import javax.swing.*;
import java.awt.*;


public class MessageService {

    public void generatePGPMessage(PGPSecretKey secretKey, PGPPublicKey publicKey, int symmetricAlgorithm,  boolean toAuth, boolean toCompress, boolean toRadix64){
        //kapiram da necemo prosledjivati neki byte[] nego lepo izaberi fajl i pozz
        PGPDataEncryptorBuilder encryptorBuilder = new BcPGPDataEncryptorBuilder(symmetricAlgorithm);
        PGPContentSignerBuilder signerBuilder = new BcPGPContentSignerBuilder(secretKey.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1);

        if(toAuth){
            /*String passPhrase = null;
            JTextField passField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Pass:"));
            panel.add(passField);
            int result = JOptionPane.showConfirmDialog(null, panel, "Enter the password", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            while (result == JOptionPane.OK_OPTION) {
                passPhrase = passField.getText();
                if (!passPhrase.equals("") && passPhrase != null ) break;
            }*/

        }

        if(symmetricAlgorithm!=0){

        }
        if(toCompress){

        }
        if(toRadix64){

        }
    }


}
