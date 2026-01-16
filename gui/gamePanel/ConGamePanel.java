package gui.gamePanel;

import gamelogic.*;
import gui.MainGUI;
import java.awt.*;
import javax.swing.*;

public class ConGamePanel extends JPanel implements GamePanelInterface, GameListener {

    private final MainGUI mainGUI;
    private final int CELL_SIZE = 30;
    private final int mapSize;

    // Referencje do przycisków, aby móc zmieniać ich kolory
    private JButton[][] playerButtons;
    private JButton[][] opponentButtons;
    
    // Logika gry
    private Game game;
    private Player player;
    private Player opponent;

    public ConGamePanel(MainGUI mainGUI, int size, String p1, String p2) {
        this.mainGUI = mainGUI;
        this.mapSize = size;
        this.playerButtons = new JButton[size][size];
        this.opponentButtons = new JButton[size][size];

        setLayout(new BorderLayout());

        // 1. Nagłówek
        JLabel headerLabel = new JLabel("Bitwa morska: " + size + "x" + size, SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // 2. Kontener Plansz (Twój układ z GridBagLayout)
        JPanel boardsContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 30, 0, 30);

        // Plansza Twoja (lewa)
        gbc.gridx = 0;
        boardsContainer.add(createBoard(size, p1, playerButtons, false), gbc);

        // Plansza Przeciwnika (prawa)
        gbc.gridx = 1;
        boardsContainer.add(createBoard(size, p2, opponentButtons, true), gbc);

        add(boardsContainer, BorderLayout.CENTER);

        // 3. Stopka z przyciskami sterującymi
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton startBtn = new JButton("Start");
        startBtn.addActionListener(e -> initGameLogic(p1, p2));
        
        JButton exitBtn = new JButton("Menu");
        exitBtn.addActionListener(e -> mainGUI.showView("MENU"));

        footer.add(startBtn);
        footer.add(exitBtn);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createBoard(int size, String name, JButton[][] matrix, boolean clickable) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        wrapper.add(nameLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(size, size));
        int totalPx = size * CELL_SIZE;
        grid.setPreferredSize(new Dimension(totalPx, totalPx));

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                JButton cell = new JButton();
                cell.setBackground(Color.CYAN);
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                
                matrix[x][y] = cell; // Zapisujemy referencję

                if (clickable) {
                    final int fx = x;
                    final int fy = y;
                    cell.addActionListener(e -> handleShot(fx, fy));
                }
                grid.add(cell);
            }
        }
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private void initGameLogic(String p1, String p2) {
        // Tutaj inicjalizujesz klasy z gamelogic (Board, Player, Game)
        // Możesz użyć metody placeRandomFleet, którą miałeś wcześniej
        System.out.println("Logika gry zainicjalizowana dla " + p1 + " vs " + p2);
    }

    private void handleShot(int x, int y) {
        // Logika strzału: game.shoot(...)
        // Po strzale kolory zaktualizują się przez metodę update(GameEvent)
        opponentButtons[x][y].setBackground(Color.BLACK); // Przykład wizualny
    }

    @Override
    public void update(GameEvent event) {
        // Tutaj pętla po matrixach i update kolorów na podstawie MapFlags
    }

    @Override
    public void display() {
        setVisible(true);
    }
}