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

        String[] columnNames = {"Name", "E-mail", "KeyID"};

        if (PrivateKeySet.getSecretKeys() == null || PrivateKeySet.getSecretKeys().size() == 0) {
            File secretFile = new File("secret.asc");
            secretFile.createNewFile();
            PrivateKeySet.importKeyPairsFromFile(secretFile);
        }

        JTable privateKeyRingTable = new JTable(PrivateKeySet.getSecretKeysMatrix(), columnNames);
        JScrollPane jScrollPane = new JScrollPane(privateKeyRingTable);
        this.add(jScrollPane, BorderLayout.CENTER);
    }
}
