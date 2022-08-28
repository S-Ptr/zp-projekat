package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ExportPrivateKeyPanel extends JPanel {

    public ExportPrivateKeyPanel() throws IOException, PGPException {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Export Private Keys"));

        this.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "E-mail", "KeyID"};

        if (PrivateKeySet.getSecretKeys() == null || PrivateKeySet.getSecretKeys().size() == 0) {
            File secretFile = new File("secret.asc");
            secretFile.createNewFile(); // if file already exists will do nothing
            PrivateKeySet.importKeyPairsFromFile(secretFile);
        }

        JTable privateKeyRingTable = new JTable(PrivateKeySet.getSecretKeysMatrix(), columnNames);

        privateKeyRingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && privateKeyRingTable.getSelectedRow() != -1) {
                    String username = privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString()
                            + " <" + privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 1).toString() + ">";
                    try {
                        PrivateKeySet.exportPairToFile(username);
                    } catch (PGPException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    Main.resetToMain();
                }
                System.out.println(privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString());
            }
        });
        JScrollPane jScrollPane = new JScrollPane(privateKeyRingTable);
        this.add(jScrollPane, BorderLayout.CENTER);
    }
}
