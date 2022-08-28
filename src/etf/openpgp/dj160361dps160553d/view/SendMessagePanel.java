package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.model.Message;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        JLabel labelName = new JLabel("Choose file to send: ");
        this.add(labelName, constraints);

        constraints.gridx = 1;
        Button buttonGenerate = new Button("Open");
        this.add(buttonGenerate, constraints);

        buttonGenerate.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to send");
            fileChooser.showOpenDialog(this);
            sendMessage.setInputFile(fileChooser.getSelectedFile());
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        for (int i = 0; i < PrivateKeySet.getSecretKeysArray().length; i++) {
            System.out.println(PrivateKeySet.getSecretKeysArray()[i]);
        }

        JComboBox<String> authenticationCombo = new JComboBox<>(PrivateKeySet.getSecretKeysArray());
        JLabel labelAuthentication = new JLabel("Choose key for authentication:");
        authenticationCombo.setEnabled(false);
        labelAuthentication.setEnabled(false);

        JCheckBox authenticationCheckBox = new JCheckBox("Enable authentication");
        authenticationCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                labelAuthentication.setEnabled(!labelAuthentication.isEnabled());
                authenticationCombo.setEnabled(!authenticationCombo.isEnabled());
            }
        });
        add(authenticationCheckBox);
        add(labelAuthentication);
        add(authenticationCombo);

    }
}
