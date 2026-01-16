package gui.gameconfig;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class PvPBoardConfigurator extends BoardConfiguratorAbstract {

    private boolean isPlayer2Active = false;
    private final JLabel titleLabel;

    public PvPBoardConfigurator(int size) {
        super(size);
        initFlota();

        setLayout(new BorderLayout(10, 10));
        titleLabel = new JLabel("GRACZ 1: Ustaw swoje statki", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        shipsPanel = new JPanel();
        shipsPanel.setLayout(new BoxLayout(shipsPanel, BoxLayout.Y_AXIS));
        shipsPanel.setBorder(BorderFactory.createTitledBorder("Statki"));
        add(shipsPanel, BorderLayout.EAST);

        updateShipsPanel();

        confirmButton = new JButton("Zatwierdź planszę Gracza 1");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> handleConfirm());
        add(confirmButton, BorderLayout.SOUTH);
    }

    private void initFlota() {
        remainingShips = new LinkedHashMap<>();
        remainingShips.put(4, 1); remainingShips.put(3, 2);
        remainingShips.put(2, 3); remainingShips.put(1, 4);
    }

    private void handleConfirm() {
        if (!isPlayer2Active) {
            JOptionPane.showMessageDialog(this, "Teraz tura Gracza 2!");
            isPlayer2Active = true;
            initFlota();
            titleLabel.setText("GRACZ 2: Ustaw swoje statki");
            confirmButton.setText("Zatwierdź i graj");
            confirmButton.setEnabled(false);
            boardPanel.clearVisuals();
            updateShipsPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Gotowe!");
            // Tutaj logika startu gry
        }
    }

    @Override
    protected boolean[][] getActiveBoard() {
        return isPlayer2Active ? player2Board : player1Board;
    }
}