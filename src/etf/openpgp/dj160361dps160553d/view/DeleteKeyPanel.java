package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.InvalidPasswordException;
import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DeleteKeyPanel extends JPanel {
    public DeleteKeyPanel() throws IOException, PGPException {
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

        privateKeyRingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && privateKeyRingTable.getSelectedRow() != -1) {

                    JPanel enterPasswordPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.insets = new Insets(10, 10, 10, 10);


                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    enterPasswordPanel.add(new JLabel("If you are sure you want to delete this key pair " +
                            "enter your password and click OK."), constraints);

                    constraints.gridy = 1;
                    enterPasswordPanel.add(new JLabel("Enter password:"), constraints);

                    constraints.gridx = 1;
                    JPasswordField passwordField = new JPasswordField(20);
                    enterPasswordPanel.add(passwordField, constraints);

                    int result = JOptionPane.showConfirmDialog(null, enterPasswordPanel, "Enter password",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        char[] password = passwordField.getPassword();
                        if (password != null && password.length != 0) {
                            try {
                                String username = privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString()
                                        + " <" + privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 1).toString() + ">";
                                Main.resetToMain();
                                JOptionPane.showMessageDialog(null, "Key successfully deleted!");
                                PrivateKeySet.removePrivateKey(username, password);
                            } catch (PGPException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                System.out.println(privateKeyRingTable.getValueAt(privateKeyRingTable.getSelectedRow(), 0).toString());
            }
        });
        JScrollPane jScrollPane = new JScrollPane(privateKeyRingTable);
        this.add(jScrollPane, BorderLayout.CENTER);
    }
}