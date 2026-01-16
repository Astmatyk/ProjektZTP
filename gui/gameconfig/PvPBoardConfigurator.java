package gui.gameconfig;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class PvPBoardConfigurator extends BoardConfiguratorAbstract {
    
    public PvPBoardConfigurator(int size) {
        super(size);
        System.out.println("PvP - TEST");
        this.player1Board = new boolean[size][size];
        this.player2Board = new boolean[size][size];

        remainingShips = new LinkedHashMap<>();
        remainingShips.put(4, 1);
        remainingShips.put(3, 2);
        remainingShips.put(2, 3);
        remainingShips.put(1, 4);

        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(900, 600));

        JLabel title = new JLabel("Ustaw swoje statki", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        boardPanel = new BoardPanel();
        centerPanel.add(boardPanel, BorderLayout.CENTER);

        shipsPanel = new JPanel();
        shipsPanel.setLayout(new BoxLayout(shipsPanel, BoxLayout.Y_AXIS));
        shipsPanel.setBorder(BorderFactory.createTitledBorder("Pozostałe statki"));
        centerPanel.add(shipsPanel, BorderLayout.EAST);

        updateShipsPanel();

        add(centerPanel, BorderLayout.CENTER);

        confirmButton = new JButton("Zatwierdź");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Plansza skonfigurowana!")
        );

        add(confirmButton, BorderLayout.SOUTH);
    }
}
