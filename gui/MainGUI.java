package gui;

import javax.swing.*;
import java.awt.*;

public class MainGUI {
    private JFrame frame;
    private JPanel cardContainer; // Panel z CardLayout
    private CardLayout cardLayout;

    public MainGUI() {
        frame = new JFrame("FigureDrawer");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // GridBagLayout na Frame sprawi, że cardContainer będzie zawsze na środku
        frame.setLayout(new GridBagLayout());
        
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        // Rejestracja paneli
        // Przekazujemy 'this' (MainGUI), aby panele mogły wysyłać sygnały do zmiany widoku
        cardContainer.add(new MainPanel(this), "MENU");
        cardContainer.add(new GameConfigPanel(), "GAME");
        cardContainer.add(new AchievementPanel(), "ACHIEVEMENTS");

        frame.add(cardContainer);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Metoda "sygnałowa", którą będą wywoływać inne panele
    public void showView(String viewName) {
        cardLayout.show(cardContainer, viewName);
    }
}