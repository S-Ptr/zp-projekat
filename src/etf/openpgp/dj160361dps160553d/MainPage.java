package etf.openpgp.dj160361dps160553d;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainPage {

    public static JFrame mainPage;

    public static void initUI() {

        mainPage = new JFrame();

        createMenuBar();
        createWelcomePage();

        mainPage.setTitle("PGP protocol");
        mainPage.setSize(500, 500);
        mainPage.setLocationRelativeTo(null);
        mainPage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPage.setVisible(true);

    }

    private static void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu keyMenu = new JMenu("Keys");
        keyMenu.setMnemonic(KeyEvent.VK_K);
        keyMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                mainPage.dispose();
                new KeysPage();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        menuBar.add(keyMenu);

        JMenu sendMenu = new JMenu("Send Message");
        sendMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(sendMenu);

        JMenu receiveMenu = new JMenu("Receive Message");
        receiveMenu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(receiveMenu);

        mainPage.setJMenuBar(menuBar);

    }

    private static void createWelcomePage() {

        JLabel welcomeLabel = new JLabel("Welcome to PGP Protocol!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPage.add(welcomeLabel);

    }

    public static void main(String[] args) {
        initUI();
    }
}
