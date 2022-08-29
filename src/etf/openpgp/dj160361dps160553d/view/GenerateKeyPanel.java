package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.model.KeyLength;
import etf.openpgp.dj160361dps160553d.model.User;
import etf.openpgp.dj160361dps160553d.service.KeyService;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;

import javax.swing.*;
import java.awt.*;

public class GenerateKeyPanel extends JPanel {
    private final JTextField fieldName = new JTextField(20);

    private final JTextField fieldEmail = new JTextField(20);
    private final JTextField fieldPassword = new JPasswordField(20);
    private final JComboBox<KeyLength> comboBoxAlgorithm = new JComboBox<>(KeyLength.values());

    public GenerateKeyPanel() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel labelName = new JLabel("Enter name: ");
        this.add(labelName, constraints);

        constraints.gridx = 1;
        this.add(fieldName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel labelEmail = new JLabel("Enter email: ");
        this.add(labelEmail, constraints);

        constraints.gridx = 1;
        this.add(fieldEmail, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(comboBoxAlgorithm, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        JLabel labelPassword = new JLabel("Enter password: ");
        this.add(labelPassword, constraints);

        constraints.gridx = 1;
        this.add(fieldPassword, constraints);

        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        Button buttonGenerate = new Button("Generate Key Pair");
        this.add(buttonGenerate, constraints);

        buttonGenerate.addActionListener(e -> {
            User user = new User(fieldName.getText(), fieldEmail.getText(),
                    comboBoxAlgorithm.getItemAt(comboBoxAlgorithm.getSelectedIndex()), fieldPassword.getText());

            if (!user.isValid()) {
                JOptionPane.showMessageDialog(null, "Please enter data!");
                return;
            }

            PGPKeyPair generatedKeyPair;
            try {
                generatedKeyPair = KeyService.generateKeyPair(new User(fieldName.getText(), fieldEmail.getText(),
                        comboBoxAlgorithm.getItemAt(comboBoxAlgorithm.getSelectedIndex()), null));
            } catch (PGPException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred during generating keys!");
                throw new RuntimeException(ex);
            }
            PrivateKeySet.addPrivateKey(generatedKeyPair, user);
            Main.resetToMain();
            JOptionPane.showMessageDialog(null, "Key successfully created!");
        });

        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Generate Key Pair"));
    }
}
