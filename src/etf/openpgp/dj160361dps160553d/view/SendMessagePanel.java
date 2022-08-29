package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.model.Message;
import etf.openpgp.dj160361dps160553d.model.SymmetricAlgorithmOptions;
import etf.openpgp.dj160361dps160553d.service.MessageService;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SendMessagePanel extends JPanel {

    public SendMessagePanel() throws PGPException, IOException {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Send Message"));

        this.setLayout(new GridBagLayout());

        Message sendMessage = new Message();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel labelFileToSend = new JLabel("Choose file to send: ");
        this.add(labelFileToSend, constraints);

        constraints.gridx = 1;
        Button buttonFileToSend = new Button("Open");
        this.add(buttonFileToSend, constraints);

        buttonFileToSend.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to send");
            fileChooser.showOpenDialog(this);
            sendMessage.setInputFile(fileChooser.getSelectedFile());
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel labelFileToCreate = new JLabel("Choose file to save to: ");
        this.add(labelFileToCreate, constraints);

        constraints.gridx = 1;
        Button buttonFileToCreate = new Button("Open");
        this.add(buttonFileToCreate, constraints);

        buttonFileToCreate.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to save to");
            fileChooser.showOpenDialog(this);
            sendMessage.setOutputFile(fileChooser.getSelectedFile());
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        //private
        JLabel labelAuthentication = new JLabel("Choose key for authentication:");
        add(labelAuthentication, constraints);

        constraints.gridx = 1;
        String[] authenticationKeys = new String[PrivateKeySet.getSecretKeysArray().length + 1];
        authenticationKeys[0] = "No authentication";
        int j = 1;
        for (int i = 0; i < PrivateKeySet.getSecretKeysArray().length; i++) {
            authenticationKeys[j] = PrivateKeySet.getSecretKeysArray()[i];
            j++;
        }
        JComboBox<String> authenticationJComboBox = new JComboBox<>(authenticationKeys);
        authenticationJComboBox.setEnabled(true);
        add(authenticationJComboBox, constraints);

        //enkripcija public
        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel labelConfidentiality = new JLabel("Choose key for confidentiality:");
        add(labelConfidentiality, constraints);

        constraints.gridx = 1;
        String[] confidentialityKeys = new String[PublicKeySet.getPublicKeysArray().length + 1];
        confidentialityKeys[0] = "No confidentiality";
        int k = 1;
        for (int i = 0; i < PublicKeySet.getPublicKeysArray().length; i++) {
            confidentialityKeys[k] = PublicKeySet.getPublicKeysArray()[i];
            k++;
        }
        JComboBox<String> confidentialityJComboBox = new JComboBox<>(confidentialityKeys);
        confidentialityJComboBox.setEnabled(true);
        add(confidentialityJComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel labelEncryption = new JLabel("Choose algorithm for encryption:");
        add(labelEncryption, constraints);
        constraints.gridx = 1;
        String[] symmetricAlgorithms = new String[3];
        symmetricAlgorithms[0] = SymmetricAlgorithmOptions.NO_SYMMETRIC_ENCRYPTION.name();
        symmetricAlgorithms[1] = SymmetricAlgorithmOptions.AES_128.name();
        symmetricAlgorithms[2] = SymmetricAlgorithmOptions.TRIPLE_DES.name();
        JComboBox<String> symmetricAlgorithmsJComboBox = new JComboBox<>(symmetricAlgorithms);
        symmetricAlgorithmsJComboBox.setEnabled(true);
        add(symmetricAlgorithmsJComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        JCheckBox compressionCheckBox = new JCheckBox("Enable compression: ");
        add(compressionCheckBox, constraints);

        constraints.gridx = 1;
        JCheckBox conversionCheckBox = new JCheckBox("Enable conversion: ");
        add(conversionCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        Button buttonSend = new Button("Send");
        this.add(buttonSend, constraints);

        buttonSend.addActionListener(e -> {
            if (authenticationJComboBox.getItemAt(authenticationJComboBox.getSelectedIndex()) != authenticationJComboBox.getItemAt(0)) {
                JPanel enterPasswordPanel = new JPanel(new GridBagLayout());
                GridBagConstraints constraintsPassword = new GridBagConstraints();
                constraintsPassword.anchor = GridBagConstraints.WEST;
                constraintsPassword.insets = new Insets(10, 10, 10, 10);

                constraintsPassword.gridx = 0;
                constraintsPassword.gridy = 0;

                enterPasswordPanel.add(new JLabel("Enter password:"), constraints);

                constraintsPassword.gridx = 1;
                JPasswordField passwordField = new JPasswordField(20);
                enterPasswordPanel.add(passwordField, constraints);

                int result = JOptionPane.showConfirmDialog(null, enterPasswordPanel, "Enter password",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    char[] password = passwordField.getPassword();
                    if (password != null && password.length != 0) {
                        try {
                            sendMessage.setSenderKey(PrivateKeySet.getKeyPair(authenticationJComboBox.getItemAt(authenticationJComboBox.getSelectedIndex()), password));
                        } catch (PGPException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }

            try {
                sendMessage.setReceiverKey(PublicKeySet.getPublicKey(confidentialityJComboBox.getItemAt(confidentialityJComboBox.getSelectedIndex())));
            } catch (PGPException ex) {
                throw new RuntimeException(ex);
            }

            sendMessage.setSymmetricAlgorithm(SymmetricAlgorithmOptions.myValueOf(symmetricAlgorithmsJComboBox.getItemAt(symmetricAlgorithmsJComboBox.getSelectedIndex())));
            sendMessage.setToAuth(sendMessage.getSenderKey() != null);
            sendMessage.setToCompress(compressionCheckBox.isSelected());
            sendMessage.setToRadix64(conversionCheckBox.isSelected());
            try {
                MessageService.generatePGPMessage(sendMessage.getSenderKey(), sendMessage.getReceiverKey(), sendMessage.getInputFile(), sendMessage.getOutputFile(),
                        sendMessage.getSymmetricAlgorithm(), sendMessage.isToAuth(), sendMessage.isToCompress(), sendMessage.isToRadix64());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(sendMessage.toString());
            Main.resetToMain();
            JOptionPane.showMessageDialog(null, "Message successfully sent!");
        });

    }
}
