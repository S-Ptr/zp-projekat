package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ViewPublicKeyRingPanel extends JPanel {

    public ViewPublicKeyRingPanel() throws PGPException, IOException {
        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        String[] columnNames = {"Name", "E-mail", "Timestamp", "KeyID"};

        if (PublicKeySet.getPublicKeys() == null) {
            File publicKeysFile = new File("public.asc");
            publicKeysFile.createNewFile(); // if file already exists will do nothing
            PublicKeySet.importKeysFromFile(publicKeysFile);
        }

        Object[][] publicKeysData = new Object[PublicKeySet.getPublicKeys().size()][4];
        int i = 0;
        PublicKeySet.getPublicKeys().getKeyRings().forEachRemaining(pgpSecretKeys -> {
            String identity = new String(pgpSecretKeys.getPublicKeys().next().getRawUserIDs().next());

            publicKeysData[i][0] = identity.split(" ")[0];
            publicKeysData[i][1] = identity.split(" ")[1].substring(1, identity.split(" ")[1].length() - 1);

            publicKeysData[i][2] = pgpSecretKeys.getPublicKeys().next().getPublicKeyPacket().getTime().toString();
            publicKeysData[i][3] = Long.toHexString(pgpSecretKeys.getPublicKeys().next().getKeyID());

        });

        JTable privateKeyRingTable = new JTable(publicKeysData, columnNames);
        JScrollPane jScrollPane = new JScrollPane(privateKeyRingTable);
        this.add(jScrollPane);
    }
}
