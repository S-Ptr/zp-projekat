package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ViewPublicKeyRingPanel extends JPanel {

    public ViewPublicKeyRingPanel() throws PGPException, IOException {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Public Keys"));

        this.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "E-mail", "KeyID"};

        if (PublicKeySet.getPublicKeys() == null || PublicKeySet.getPublicKeys().size() == 0) {
            File publicKeysFile = new File("public.asc");
            publicKeysFile.createNewFile(); // if file already exists will do nothing
            PublicKeySet.importKeysFromFile(publicKeysFile);
        }

        JTable privateKeyRingTable = new JTable(PublicKeySet.getPublicKeysMatrix(), columnNames);
        JScrollPane jScrollPane = new JScrollPane(privateKeyRingTable);
        this.add(jScrollPane, BorderLayout.CENTER);

    }
}
