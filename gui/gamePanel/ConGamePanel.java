package gui.gamePanel;

import gui.MainGUI;
import java.awt.*;
import javax.swing.*;

public class ConGamePanel extends JPanel implements GamePanelInterface {

    private MainGUI mainGUI;
    private final int CELL_SIZE = 30; // Stały rozmiar komórki
        
    public ConGamePanel(MainGUI mainGUI, int size, String p1, String p2) {
        this.mainGUI = mainGUI;
            
        this.setLayout(new BorderLayout());

        //------------------------------------------------Nagłówek------------------------------------------------
        JLabel headerLabel = new JLabel("Rozmiar mapy: " + size + "x" + size, SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(headerLabel, BorderLayout.NORTH);

        //------------------------------------------------Kontener Plansz------------------------------------------------
        // Panel z GridBagLayout, który wyśrodkuje plansze w pionie i poziomie
        JPanel boardsContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20); // Odstęp między planszami

        // Budowanie planszy Gracza 1
        JPanel player1Board = createBoard(size, p1);
        gbc.gridx = 0;
        boardsContainer.add(player1Board, gbc);

        // Budowanie planszy Gracza 2 (lub Komputera)
        JPanel player2Board = createBoard(size, p2);
        gbc.gridx = 1;
        boardsContainer.add(player2Board, gbc);

        this.add(boardsContainer, BorderLayout.CENTER);

        //------------------------------------------------Stopka------------------------------------------------
        JButton exitBtn = new JButton("Wyjdź do menu");
        exitBtn.addActionListener(al -> mainGUI.showView("MENU"));
        
        // Panel pomocniczy dla przycisku, żeby nie zajmował całej szerokości
        JPanel footerPanel = new JPanel();
        footerPanel.add(exitBtn);
        this.add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createBoard(int size, String playerName) {
        JPanel wrapper = new JPanel(new BorderLayout());
        
        // Nick nad planszą
        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        wrapper.add(nameLabel, BorderLayout.NORTH);

        // Siatka o stałym rozmiarze
        JPanel grid = new JPanel(new GridLayout(size, size));
        int totalPx = size * CELL_SIZE;
        grid.setPreferredSize(new Dimension(totalPx, totalPx));
        grid.setBackground(Color.BLACK);
        grid.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Wypełnienie siatki przyciskami/polami
        for (int i = 0; i < size * size; i++) {
            JButton cell = new JButton();
            cell.setBackground(Color.WHITE);
            cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
            // Tutaj w przyszłości dodasz ActionListenera do strzelania
            grid.add(cell);
        }

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    @Override
    public void display() {
        setVisible(true);
    }
}