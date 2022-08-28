package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.model.Message;
import etf.openpgp.dj160361dps160553d.model.User;
import etf.openpgp.dj160361dps160553d.service.KeyService;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

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
        for (int i = 0; i < PrivateKeySet.getSecretKeysArray().size(); i++) {
            System.out.println(PrivateKeySet.getSecretKeysArray().get(i));
        }
        JComboBox<String> authenticationCombo = new JComboBox<>(PrivateKeySet.getSecretKeysArray().toArray(new String[PrivateKeySet.getSecretKeysArray().size()]));
        JLabel labelAuthentication = new JLabel("Choose key for authentication:");
        authenticationCombo.setEnabled(false);
        labelAuthentication.setEnabled(false);
        add(authenticationCombo, constraints);

    }
}
