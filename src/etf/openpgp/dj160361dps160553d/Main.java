package etf.openpgp.dj160361dps160553d;

import etf.openpgp.dj160361dps160553d.view.GenerateKeyPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static JFrame mainFrame;
    public static GenerateKeyPanel generateKeyPanel;

    public static void main(String[] args) {

        mainFrame = new JFrame("PGP protocol");
        mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));

        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);

        JMenu keyPairMenu = new JMenu("Key Pairs");
        menuBar.add(keyPairMenu);

        AbstractAction generateKeyPairAction = new AbstractAction("Generate Key Pair") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Generate");
                generateKeyPanel = new GenerateKeyPanel();
                mainFrame.add(generateKeyPanel);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setBounds(600, 300, 700, 500);
            }
        };
        JMenuItem generateKeyPairMenuItem = new JMenuItem(generateKeyPairAction);
        keyPairMenu.add(generateKeyPairMenuItem);

        AbstractAction deleteKeyPairAction = new AbstractAction("Delete Key Pair") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.printf("Delete");
            }
        };
        JMenuItem deleteKeyPairMenuItem = new JMenuItem(deleteKeyPairAction);
        keyPairMenu.add(deleteKeyPairMenuItem);

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        AbstractAction viewPublicKeyRingAction = new AbstractAction("Public Key Ring") {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKeyPanel = new GenerateKeyPanel();
                mainFrame.add(generateKeyPanel);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setBounds(600, 300, 700, 500);
            }
        };
        JMenuItem viewPublicKeyRingMenu = new JMenuItem(viewPublicKeyRingAction);
        viewMenu.add(viewPublicKeyRingMenu);

        AbstractAction viewSecretKeyRingAction = new AbstractAction("Secret Key Ring") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.printf("Delete");
            }
        };
        JMenuItem viewSecretKeyRingMenu = new JMenuItem(viewSecretKeyRingAction);
        viewMenu.add(viewSecretKeyRingMenu);

        mainFrame.setBounds(600, 300, 700, 500);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
