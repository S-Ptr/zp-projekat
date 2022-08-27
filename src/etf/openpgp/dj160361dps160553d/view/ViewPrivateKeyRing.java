package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.PrivateKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ViewPrivateKeyRing extends JPanel {

    public ViewPrivateKeyRing() throws PGPException, IOException {
        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        String[] columnNames = {"Name", "E-mail", "Timestamp", "KeyID"};

        if (PrivateKeySet.getSecretKeys() == null) {
            PrivateKeySet.importKeyPairsFromFile(true);
        }

        Object[][] data = new Object[PrivateKeySet.getSecretKeys().size()][4];
        int i = 0;
        PrivateKeySet.getSecretKeys().getKeyRings().forEachRemaining(pgpSecretKeys -> {
            String identity = new String(pgpSecretKeys.getPublicKeys().next().getRawUserIDs().next());

            data[i][0] = identity.split(" ")[0];
            data[i][1] = identity.split(" ")[1].substring(1, identity.split(" ")[1].length() - 1);

            data[i][2] = pgpSecretKeys.getPublicKeys().next().getPublicKeyPacket().getTime().toString();
            data[i][3] = Long.toHexString(pgpSecretKeys.getPublicKeys().next().getKeyID());

        });

        JTable privateKeyRingTable = new JTable(data, columnNames);
    }
}
