package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ExportPublicKeyPanel extends JTabbedPane {

    public ExportPublicKeyPanel() throws IOException, PGPException {
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Export Keys"));

        JPanel privateKeysPanel = new JPanel();
        privateKeysPanel.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "E-mail", "KeyID"};

        //PRIVATE KEYS

        if (PrivateKeySet.getSecretKeys() == null || PrivateKeySet.getSecretKeys().size() == 0) {
            File secretFile = new File("secret.asc");
            secretFile.createNewFile();
            PrivateKeySet.importKeyPairsFromFile(secretFile);
        }

        JTable privateKeyRingTable = new JTable(PrivateKeySet.getSecretKeysMatrix(), columnNames);

        privateKeyRingTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && privateKeyRingTable.getSelectedRow() != -1) {
                String username = privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString()
                        + " <" + privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 1).toString() + ">";
                try {
                    PrivateKeySet.exportPublicKey(username);
                } catch (PGPException | IOException e) {
                    throw new RuntimeException(e);
                }
                Main.resetToMain();
                JOptionPane.showMessageDialog(null, "Key successfully exported!");
            }
            System.out.println(privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString());
        });
        privateKeysPanel.add(new JScrollPane(privateKeyRingTable), BorderLayout.SOUTH);

        //PUBLIC KEYS
        JPanel publicKeysPanel = new JPanel();
        publicKeysPanel.setLayout(new BorderLayout());

        if (PublicKeySet.getPublicKeys() == null || PublicKeySet.getPublicKeys().size() == 0) {
            File publicKeysFile = new File("public.asc");
            publicKeysFile.createNewFile();
            PublicKeySet.importKeysFromFile(publicKeysFile);
        }

        JTable publicKeyRingTable = new JTable(PublicKeySet.getPublicKeysMatrix(), columnNames);

        publicKeyRingTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && publicKeyRingTable.getSelectedRow() != -1) {
                String username = publicKeyRingTable.getValueAt(publicKeyRingTable.getSelectedRow(), 0).toString()
                        + " <" + publicKeyRingTable.getValueAt(publicKeyRingTable.getSelectedRow(), 1).toString() + ">";
                try {
                    PublicKeySet.exportKeyToFile(username);
                } catch (PGPException | IOException e) {
                    throw new RuntimeException(e);
                }
                Main.resetToMain();
                JOptionPane.showMessageDialog(null, "Key successfully exported!");
            }
            System.out.println(privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString());
        });

        publicKeysPanel.add(new JScrollPane(publicKeyRingTable), BorderLayout.SOUTH);

        add(privateKeysPanel, "Secret Keys");
        add(publicKeysPanel, "Public Keys");
    }
}
