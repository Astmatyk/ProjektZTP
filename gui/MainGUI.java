package gui;

import gui.gamePanel.ConGamePanel;
import java.awt.*;
import javax.swing.*;

public class MainGUI {
    private JFrame frame;
    private JPanel cardContainer; // Panel z CardLayout
    private CardLayout cardLayout;

    public MainGUI() {
        frame = new JFrame("Gra w statki. Aplikacja pozwala na rozgrywkę między dwoma graczami lub z komputerem. Grający rozmieszczają swoje floty na planszy (zgodnie z zasadami gry), a następnie na przemian oddają strzały, próbując zatopić wszystkie statki przeciwnika. Gra oferuje tryb standardowy z określonymi zasadami (np. rozmiary planszy, typy statków) oraz prostą logikę komputerowego przeciwnika, która może być rozbudowana o różne poziomy trudności. Aplikacja zachowuje historię rozgrywek, pozwalając na przeglądanie statystyk, w tym rankingu graczy. Gra oferuje personalizację wyglądu planszy i statków, system osiągnięć oraz odblokowywanie dodatkowej zawartości (np. nowych wzorów statków, trybów gry). Tryb symulacji pozwala obserwować grę komputera przeciwko samemu sobie.");
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
        cardContainer.add(new ConGamePanel(this), "GAME");
        cardContainer.add(new AchievementPanel(this), "ACHIEVEMENT");
        cardContainer.add(new RankingPanel(this), "RANKING");

        frame.add(cardContainer);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Metoda "sygnałowa", którą będą wywoływać inne panele
    public void showView(String viewName) {
        cardLayout.show(cardContainer, viewName);
    }
}