package etf.openpgp.dj160361dps160553d.view;

import etf.openpgp.dj160361dps160553d.Main;
import etf.openpgp.dj160361dps160553d.model.Message;
import etf.openpgp.dj160361dps160553d.service.MessageService;
import etf.openpgp.dj160361dps160553d.service.PrivateKeySet;
import etf.openpgp.dj160361dps160553d.service.PublicKeySet;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ReceiveMessagePanel extends JPanel {
    public ReceiveMessagePanel() {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Receive Message"));

        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel labelFileToSend = new JLabel("Choose file to receive: ");
        this.add(labelFileToSend, constraints);

        constraints.gridx = 1;
        Button buttonFileToSend = new Button("Open");
        this.add(buttonFileToSend, constraints);

        buttonFileToSend.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to send");
            fileChooser.showOpenDialog(this);
            try {
                MessageService.readPGPMessage(fileChooser.getSelectedFile(), PrivateKeySet.getSecretKeys(), PublicKeySet.getPublicKeys());
            } catch (IOException | PGPException ex) {
                throw new RuntimeException(ex);
            }
            Main.resetToMain();
        });
    }
}
