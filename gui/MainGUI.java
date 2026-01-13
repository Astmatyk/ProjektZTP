package gui;

import javax.swing.*;

import gui.gamePanel.ConGamePanel;

import java.awt.*;

public class MainGUI {
    private JFrame frame;
    private JPanel cardContainer; // Panel z CardLayout
    private CardLayout cardLayout;

    public MainGUI() {
        frame = new JFrame("FigureDrawer");
        // Pobieramy URL do zasobu
        java.net.URL iconURL = getClass().getResource("graphics/logo.png");

        if (iconURL != null) {
            // Jeśli plik istnieje, ustaw ikonę
            frame.setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            // Jeśli nie istnieje, wypisz błąd w konsoli zamiast wywalać program
            System.err.println("BŁĄD: Nie znaleziono pliku ikony pod adresem: graphics/logo.png");
            System.err.println("Upewnij się, że plik jest w folderze graphics");
        }

        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // GridBagLayout na Frame sprawi, że cardContainer będzie zawsze na środku
        frame.setLayout(new GridBagLayout());
        
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        // Rejestracja paneli
        // Przekazujemy 'this' (MainGUI), aby panele mogły wysyłać sygnały do zmiany widoku
        cardContainer.add(new MainPanel(this), "MENU");
        cardContainer.add(new GameConfigPanel(this), "GAME_CONFIG");
        cardContainer.add(new ConGamePanel(), "GAME");
        cardContainer.add(new AchievementPanel(this), "ACHIEVEMENTS");

        frame.add(cardContainer);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Metoda "sygnałowa", którą będą wywoływać inne panele
    public void showView(String viewName) {
        cardLayout.show(cardContainer, viewName);
    }
}