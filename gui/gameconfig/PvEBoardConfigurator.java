package gui.gameconfig;

import java.awt.*;
import javax.swing.*;

public class PvEBoardConfigurator extends BoardConfiguratorAbstract {

    public PvEBoardConfigurator(int size) {
        super(size);
        
        // 1. Losujemy statki dla komputera (P2)
        this.player2Board = placeRandomFleet(size);

        // 2. Inicjalizujemy listę statków do postawienia dla Gracza 1
        initRemainingShips();

        // 3. Korzystamy z gotowej metody fillUI z klasy bazowej
        // To ona stworzy 'new BoardPanel(player1Board)' za Ciebie!
        fillUI(player1Board, "Tryb PvE: Ustaw swoje statki", "Zatwierdź i rozpocznij grę");

        // 4. Dodajemy akcję dla przycisku (specyficzną dla PvE)
        confirmButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Plansza skonfigurowana! Komputer również gotowy.");
            // Tutaj wstawisz kod przejścia do gry
        });
        
        System.out.println("PvE Configurator initialized.");
    }
}