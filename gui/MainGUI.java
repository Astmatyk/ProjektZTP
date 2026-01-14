package gui;

import gui.gamePanel.ConGamePanel;
import gui.gamePanel.EvEGamePanel;
import gui.gamePanel.GamePanelInterface;
import gui.gamePanel.PvEGamePanel;
import gui.gamePanel.PvPGamePanel;
import java.awt.*;
import javax.swing.*;

public class MainGUI {
    private JFrame frame;
    private JPanel cardContainer;
    private CardLayout cardLayout;

    public MainGUI() {

        // Utworzenie okna
        frame = new JFrame("Statki");

        // Obsługa logo
        // Pobieramy URL do zasobu
        java.net.URL iconURL = getClass().getResource("/gui/graphics/logo.png");

        if (iconURL != null) {
            // Jeśli plik istnieje, ustaw ikonę
            frame.setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            // Jeśli nie istnieje, wypisz błąd w konsoli zamiast wywalać program
            System.err.println("BŁĄD: Nie znaleziono pliku ikony pod adresem: graphics/logo.png");
            System.err.println("Upewnij się, że plik jest w folderze graphics");
        }

        // Rozmiar okna i akcja przy zamknięciu
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        // Rejestracja paneli
        // Przekazujemy 'this' (MainGUI), aby panele mogły wysyłać sygnały do zmiany widoku
        cardContainer.add(new MainPanel(this), "MENU");
        cardContainer.add(new GameConfigPanel(this), "GAME_CONFIG");
        cardContainer.add(new AchievementPanel(this), "ACHIEVEMENT");
        cardContainer.add(new RankingPanel(this), "RANKING");

        frame.add(cardContainer, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showView("MENU");
    }

    // Metoda "sygnałowa", którą będą wywoływać inne panele
    public void showView(String viewName) {
        cardLayout.show(cardContainer, viewName);
    }

    public void launchGame(int boardSize, String mode, String p1Name, String p2Name) {
    // 1. Tworzymy bazę (Core)
    GamePanelInterface game = new ConGamePanel(this, boardSize, p1Name, p2Name);

    // 2. Dekorujemy w zależności od trybu
    switch(mode){
        case "PVP":
            game = new PvPGamePanel(game);
            break;
        case "PVE":
            game = new PvEGamePanel(game);
            break;
        case "EVE":
            game = new EvEGamePanel(game);
            break;
    }
    
    // 3. Dodajemy do UI (rzutujemy na JPanel, bo cardContainer tego wymaga)
    cardContainer.add((JPanel)game, "GAME");
    
    cardContainer.revalidate();
    cardContainer.repaint();
    
    showView("GAME");
}
}