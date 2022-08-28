package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ViewPrivateKeyRingPanel extends JPanel {

    public ViewPrivateKeyRingPanel() throws PGPException, IOException {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Private Keys"));

        this.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "E-mail", "Timestamp", "KeyID"};

        if (PrivateKeySet.getSecretKeys() == null || PrivateKeySet.getSecretKeys().size() == 0) {
            File secretFile = new File("secret.asc");
            secretFile.createNewFile(); // if file already exists will do nothing
            PrivateKeySet.importKeyPairsFromFile(secretFile);
        }

        Object[][] privateKeyData = new Object[PrivateKeySet.getSecretKeys().size()][4];
        int i = 0;
        PrivateKeySet.getSecretKeys().getKeyRings().forEachRemaining(pgpSecretKeys -> {
            String identity = new String(pgpSecretKeys.getPublicKeys().next().getRawUserIDs().next());

            privateKeyData[i][0] = identity.split(" ")[0];
            privateKeyData[i][1] = identity.split(" ")[1].substring(1, identity.split(" ")[1].length() - 1);

            privateKeyData[i][2] = pgpSecretKeys.getPublicKeys().next().getPublicKeyPacket().getTime().toString();
            privateKeyData[i][3] = Long.toHexString(pgpSecretKeys.getPublicKeys().next().getKeyID());

        });

        JTable privateKeyRingTable = new JTable(privateKeyData, columnNames);
        JScrollPane jScrollPane = new JScrollPane(privateKeyRingTable);
        this.add(jScrollPane, BorderLayout.CENTER);

    }
}
