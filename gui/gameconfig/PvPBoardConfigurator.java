package gui.gameconfig;

import javax.swing.*;

public class PvPBoardConfigurator extends BoardConfiguratorAbstract {

    private boolean isPlayer2Active = false;

    public PvPBoardConfigurator(int size) {
        super(size);
        System.out.println("Initializing PvP Configurator...");
        initRemainingShips();
        
        // Budujemy interfejs dla pierwszego gracza
        fillUI(player1Board, "GRACZ 1: Ustaw swoje statki", "Zatwierdź planszę Gracza 1");
        
        // Podpinamy specyficzną logikę pod przycisk
        confirmButton.addActionListener(e -> handleConfirm());
    }

    private void handleConfirm() {
        if (!isPlayer2Active) {
            JOptionPane.showMessageDialog(this, "Gracz 1 gotowy. Teraz tura Gracza 2!");
            
            isPlayer2Active = true;
            initRemainingShips();
            selectedShipLength = -1;

            // RESTART OKNA DLA GRACZA 2
            fillUI(player2Board, "GRACZ 2: Ustaw swoje statki", "Zatwierdź i rozpocznij grę");
            
            // Ponowne podpięcie listenera (bo fillUI stworzył nowy przycisk)
            confirmButton.addActionListener(e -> handleConfirm());
        } else {
            JOptionPane.showMessageDialog(this, "Obie plansze skonfigurowane!");
            // Tutaj np. callback do MainGUI
        }
    }
}