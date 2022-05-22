package etf.openpgp.dj160361dps160553d;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.KeyEvent;

public class KeysPage {
    KeysPage() {
        MainPage.mainPage = new JFrame();
        initUI();
        MainPage.mainPage.setVisible(true);
    }

    private void initUI() {

        createMenuBar();
        createKeysPage();

        MainPage.mainPage.setTitle("PGP protocol");
        MainPage.mainPage.setSize(500, 500);
        MainPage.mainPage.setLocationRelativeTo(null);
        MainPage.mainPage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private static void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu createMenu = new JMenu("Create");
        createMenu.setMnemonic(KeyEvent.VK_C);
        menuBar.add(createMenu);

        JMenu deleteMenu = new JMenu("Delete");
        deleteMenu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(deleteMenu);

        JMenu importMenu = new JMenu("Import");
        importMenu.setMnemonic(KeyEvent.VK_I);
        menuBar.add(importMenu);

        JMenu exportMenu = new JMenu("Export");
        exportMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(exportMenu);

        JMenu backMenu = new JMenu("Back");
        backMenu.setMnemonic(KeyEvent.VK_B);
        backMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                MainPage.mainPage.dispose();
                MainPage.initUI();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        menuBar.add(backMenu);

        MainPage.mainPage.setJMenuBar(menuBar);

    }

    private static void createKeysPage() {

        JLabel keysLabel = new JLabel("What are we going to do with keys?");
        keysLabel.setHorizontalAlignment(SwingConstants.CENTER);
        MainPage.mainPage.add(keysLabel);

    }

}
