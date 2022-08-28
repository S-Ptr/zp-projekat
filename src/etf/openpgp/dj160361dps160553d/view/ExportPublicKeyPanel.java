package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ExportPublicKeyPanel extends JPanel {

    public ExportPublicKeyPanel() throws IOException, PGPException {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Export Public Keys"));

        this.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "E-mail", "KeyID"};

        //PRIVATE KEYS

        if (PrivateKeySet.getSecretKeys() == null || PrivateKeySet.getSecretKeys().size() == 0) {
            File secretFile = new File("secret.asc");
            secretFile.createNewFile(); // if file already exists will do nothing
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
            }
            System.out.println(privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString());
        });
        this.add(new JScrollPane(privateKeyRingTable), BorderLayout.NORTH);

        //PUBLIC KEYS
        if (PublicKeySet.getPublicKeys() == null || PublicKeySet.getPublicKeys().size() == 0) {
            File publicKeysFile = new File("public.asc");
            publicKeysFile.createNewFile(); // if file already exists will do nothing
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
            }
            System.out.println(privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString());
        });

        this.add(new JScrollPane(publicKeyRingTable), BorderLayout.SOUTH);

    }
}
