package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.model.KeyLength;
import etf.openpgp.dj160361dps160553d.model.User;
import etf.openpgp.dj160361dps160553d.service.KeyService;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;

public class GenerateKeyPanel extends JPanel {
    private final JLabel labelName = new JLabel("Enter name: ");
    private final JLabel labelEmail = new JLabel("Enter email: ");
    private final JTextField fieldName = new JTextField(20);
    private final JTextField fieldEmail = new JTextField(20);
    private final JComboBox<KeyLength> comboBoxAlgorithm = new JComboBox<>(KeyLength.values());

    private final Button buttonGenerate = new Button("Generate Key Pair");

    public GenerateKeyPanel() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(labelName, constraints);

        constraints.gridx = 1;
        this.add(fieldName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(labelEmail, constraints);

        constraints.gridx = 1;
        this.add(fieldEmail, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(comboBoxAlgorithm, constraints);

        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(buttonGenerate, constraints);

        buttonGenerate.addActionListener(e -> {
            try {
                KeyService.generateKeyPair(new User(fieldName.getText(), fieldEmail.getText(),
                        comboBoxAlgorithm.getItemAt(comboBoxAlgorithm.getSelectedIndex()), null));
            } catch (PGPException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Generate Key Pair"));
    }
}
