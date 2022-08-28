package etf.openpgp.dj160361dps160553d;

import etf.openpgp.dj160361dps160553d.model.Panels;
import etf.openpgp.dj160361dps160553d.view.GenerateKeyPanel;
import etf.openpgp.dj160361dps160553d.view.SendMessagePanel;
import etf.openpgp.dj160361dps160553d.view.ViewPrivateKeyRingPanel;
import etf.openpgp.dj160361dps160553d.view.ViewPublicKeyRingPanel;
import org.bouncycastle.openpgp.PGPException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Main {
    public static JFrame mainFrame;
    public static Panels currentPanel;
    public static GenerateKeyPanel generateKeyPanel;

    public static ViewPrivateKeyRingPanel viewPrivateKeyRingPanel;

    public static ViewPublicKeyRingPanel viewPublicKeyRingPanel;

    public static SendMessagePanel sendMessagePanel;

    public static void main(String[] args) {

        mainFrame = new JFrame("PGP protocol");
        mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));

        currentPanel = Panels.MAIN;

        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);

        JMenu keyPairMenu = new JMenu("Key Pairs");
        menuBar.add(keyPairMenu);

        AbstractAction generateKeyPairAction = new AbstractAction("Generate Key Pair") {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKeyPanel = new GenerateKeyPanel();
                if (swapPanel(Panels.GENERATE_KEY_PAIR)) {
                    mainFrame.add(generateKeyPanel);
                    mainFrame.invalidate();
                    mainFrame.revalidate();
                } else {
                    mainFrame.add(generateKeyPanel);
                }
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
                System.out.print("Delete");
            }
        };
        JMenuItem deleteKeyPairMenuItem = new JMenuItem(deleteKeyPairAction);
        keyPairMenu.add(deleteKeyPairMenuItem);

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        AbstractAction viewPublicKeyRingAction = new AbstractAction("Public Key Ring") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viewPublicKeyRingPanel = new ViewPublicKeyRingPanel();
                } catch (PGPException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (swapPanel(Panels.VIEW_PUBLIC_KEY_RING)) {
                    mainFrame.add(viewPublicKeyRingPanel);
                    mainFrame.invalidate();
                    mainFrame.revalidate();
                } else {
                    mainFrame.add(viewPublicKeyRingPanel);
                }
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
                try {
                    viewPrivateKeyRingPanel = new ViewPrivateKeyRingPanel();
                } catch (PGPException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (swapPanel(Panels.VIEW_PRIVATE_KEY_RING)) {
                    mainFrame.add(viewPrivateKeyRingPanel);
                    mainFrame.invalidate();
                    mainFrame.revalidate();
                } else {
                    mainFrame.add(viewPrivateKeyRingPanel);
                }
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setBounds(600, 300, 700, 500);
            }
        };
        JMenuItem viewSecretKeyRingMenu = new JMenuItem(viewSecretKeyRingAction);
        viewMenu.add(viewSecretKeyRingMenu);

        JMenu importMenu = new JMenu("Import");
        menuBar.add(importMenu);
        AbstractAction importPublicKey = new AbstractAction("Import Public Key") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        JMenuItem importPublicKeyMenu = new JMenuItem(importPublicKey);
        importMenu.add(importPublicKeyMenu);

        AbstractAction importPrivateKey = new AbstractAction("Import Private Key") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        JMenuItem importPrivateKeyMenu = new JMenuItem(importPrivateKey);
        importMenu.add(importPrivateKeyMenu);


        JMenu exportMenu = new JMenu("Export");
        menuBar.add(exportMenu);
        AbstractAction exportPublicKey = new AbstractAction("Export Public Key") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        JMenuItem exportPublicKeyMenu = new JMenuItem(exportPublicKey);
        importMenu.add(exportPublicKeyMenu);

        AbstractAction exportPrivateKey = new AbstractAction("Export Private Key") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        JMenuItem exportPrivateKeyMenu = new JMenuItem(exportPrivateKey);
        importMenu.add(exportPrivateKeyMenu);


        JMenu messageMenu = new JMenu("Message");
        menuBar.add(messageMenu);
        AbstractAction sendMessage = new AbstractAction("Send Message") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessagePanel = new SendMessagePanel();
                } catch (PGPException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (swapPanel(Panels.SEND_MESSAGE)) {
                    mainFrame.add(sendMessagePanel);
                    mainFrame.invalidate();
                    mainFrame.revalidate();
                } else {
                    mainFrame.add(sendMessagePanel);
                }
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setBounds(600, 300, 700, 500);
            }
        };
        JMenuItem sendMessageMenu = new JMenuItem(sendMessage);
        messageMenu.add(sendMessageMenu);

        AbstractAction receiveMessage = new AbstractAction("Receive Message") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        JMenuItem receiveMessageMenu = new JMenuItem(receiveMessage);
        messageMenu.add(receiveMessageMenu);

        mainFrame.setBounds(600, 300, 700, 500);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static boolean swapPanel(Panels nextPanel) {
        if (currentPanel == nextPanel) return false;
        boolean result = false;
        switch (currentPanel) {
            case MAIN, DELETE_KEY_PAIR -> {
                currentPanel = nextPanel;
            }
            case GENERATE_KEY_PAIR -> {
                mainFrame.remove(generateKeyPanel);
                currentPanel = nextPanel;
                result = true;
            }
            case VIEW_PUBLIC_KEY_RING -> {
                mainFrame.remove(viewPublicKeyRingPanel);
                currentPanel = nextPanel;
                result = true;
            }
            case VIEW_PRIVATE_KEY_RING -> {
                mainFrame.remove(viewPrivateKeyRingPanel);
                currentPanel = nextPanel;
                result = true;
            }
            case SEND_MESSAGE -> {
                mainFrame.remove(sendMessagePanel);
                currentPanel = nextPanel;
                result = true;
            }
        }
        return result;
    }
}
