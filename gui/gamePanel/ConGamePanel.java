package gui.gamePanel;

import gamelogic.*;
import gui.MainGUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
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

    private JPanel wrapWithTitled(String title, JPanel p) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }

    private void startGame() {
        // (re)build UI grids
        ownButtons = new JButton[mapSize][mapSize];
        oppButtons = new JButton[mapSize][mapSize];

        ownBoardPanel.removeAll();
        oppBoardPanel.removeAll();

        ownBoardPanel.setLayout(new GridLayout(mapSize, mapSize,1,1));
        oppBoardPanel.setLayout(new GridLayout(mapSize, mapSize,1,1));

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                // Own cell button inside a wrapper so we can keep it square
                JButton ownCell = new JButton();
                ownCell.setMargin(new Insets(0,0,0,0));
                ownCell.setBackground(Color.CYAN);
                ownCell.setOpaque(true);
                ownCell.setFocusPainted(false);
                ownCell.setBorderPainted(false);

                JPanel ownWrapper = new JPanel(new GridBagLayout());
                ownWrapper.add(ownCell);
                ownWrapper.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        int s = Math.min(ownWrapper.getWidth(), ownWrapper.getHeight());
                        if (s > 0) {
                            ownCell.setPreferredSize(new Dimension(s - 4, s - 4));
                            ownWrapper.revalidate();
                        }
                    }
                });

                ownButtons[x][y] = ownCell;
                ownBoardPanel.add(ownWrapper);

                // Opponent cell button inside a wrapper so we can keep it square
                JButton oppCell = new JButton();
                oppCell.setMargin(new Insets(0,0,0,0));
                oppCell.setBackground(Color.CYAN);
                oppCell.setOpaque(true);
                oppCell.setFocusPainted(false);
                oppCell.setBorderPainted(false);
                final int fx = x, fy = y;
                oppCell.addActionListener(e -> handlePlayerShot(fx, fy));

                JPanel oppWrapper = new JPanel(new GridBagLayout());
                oppWrapper.add(oppCell);
                oppWrapper.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        int s = Math.min(oppWrapper.getWidth(), oppWrapper.getHeight());
                        if (s > 0) {
                            oppCell.setPreferredSize(new Dimension(s - 4, s - 4));
                            oppWrapper.revalidate();
                        }
                    }
                });

                oppButtons[x][y] = oppCell;
                oppBoardPanel.add(oppWrapper);
            }
        }

        ownBoardPanel.revalidate();
        oppBoardPanel.revalidate();
        ownBoardPanel.repaint();
        oppBoardPanel.repaint();

        // build game backend with random fleets
        Board b1 = new Board(mapSize);
        Board b2 = new Board(mapSize);

        placeRandomFleet(b1);
        placeRandomFleet(b2);
        System.out.println("PvE = "+isPvE+"; PvP = "+isPvP+"; EvE = "+isEvE);
        if(!isEvE) {
            humanPlayer = new HumanPlayer(b1, new Board(mapSize), "Player");
        } else {
            humanPlayer = new PcPlayer(b1, new Board(mapSize), new HardMode());
        }
        if (isPvP) {
            opponent = new HumanPlayer(b2, new Board(mapSize), "Player2");
        } else {
            opponent = new PcPlayer(b2, new Board(mapSize), new HardMode());
        }

        game = new Game(humanPlayer, opponent);
        game.addListener(this);

        System.out.println("Gra rozpoczęta. Runda: Gracz");
        updateBoards();

        // If first turn is computer (EvE) handle it
        if (!(game.getCurrentPlayer() instanceof HumanPlayer)) {
            schedulePcMove();
        }
    }

    private void handlePlayerShot(int x, int y) {
        if (game == null) return;
        if (game.isGameOver()) return;
        Player current = game.getCurrentPlayer();
        if (!(current instanceof HumanPlayer)) {
            JOptionPane.showMessageDialog(ownBoardPanel, "POCZEKAJ CYMBALE");
            System.out.println("Nie twoja tura");
            return;
        }

        try {
            ShotResult res = game.shoot(current, new Coordinates(x,y));
            System.out.println("Wynik strzału: " + res);
            updateBoards();
        } catch (Exception ex) {
            System.out.println("Błąd: " + ex.getMessage());
        }

        if (!game.isGameOver() && !(game.getCurrentPlayer() instanceof HumanPlayer)) {
            schedulePcMove();
        }

        if (game.isGameOver()) {
            System.out.println("Koniec gry. Zwycięzca: " + ((HumanPlayer)game.getCurrentPlayer()).toString());
        }
    }

    private void schedulePcMove() {
        Timer t = new Timer(opponentDelay, null);
        t.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game == null || game.isGameOver()) {
                    t.stop();
                    return;
                }
                Player cur = game.getCurrentPlayer();
                if (cur instanceof PcPlayer) {
                    Coordinates c = cur.chooseCoordinates();
                    try {
                        ShotResult res = game.shoot(cur, c);
                        System.out.println("Komputer strzelił: ("+c.x+","+c.y+") -> " + res);
                        updateBoards();
                    } catch (Exception ex) {
                        // ignore and try next time
                    }
                }

                // stop timer when it's human's turn or game over
                if (game.getCurrentPlayer() instanceof HumanPlayer || game.isGameOver()) {
                    t.stop();
                }
            }
        });
        t.setInitialDelay(opponentDelay);
        t.start();
    }

    private void updateBoards() {
        if (game == null) return;

        Board own = humanPlayer.getOwnBoard();
        Board shoot = humanPlayer.getShootingBoard();
        Board oppOwn = opponent.getOwnBoard();

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                MapFlags fOwn = own.getFlag(x,y);
                JButton bOwn = ownButtons[x][y];
                switch (fOwn) {
                    case NOTHING -> bOwn.setBackground(Color.CYAN);
                    case SHIP -> bOwn.setBackground(Color.GRAY);
                    case SHIP_WRECKED -> bOwn.setBackground(Color.RED);
                    case NO_SHIP -> bOwn.setBackground(Color.BLACK);
                    default -> bOwn.setBackground(Color.PINK);
                }

                MapFlags fShot = shoot.getFlag(x,y);
                JButton bOpp = oppButtons[x][y];
                switch (fShot) {
                    case NOTHING -> bOpp.setBackground(Color.CYAN);
                    case SHIP_WRECKED -> bOpp.setBackground(Color.RED);
                    case NO_SHIP -> bOpp.setBackground(Color.BLACK);
                    default -> bOpp.setBackground(Color.PINK);
                }
            }
        }
        repaint();
    }

    private void placeRandomFleet(Board board) {
        int[] ships = new int[]{4,3,3,2,2,2,1,1,1,1};
        Random rnd = new Random();
        for (int len : ships) {
            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < 500) {
                attempts++;
                int x = rnd.nextInt(board.getSize());
                int y = rnd.nextInt(board.getSize());
                Board.Direction dir = rnd.nextBoolean() ? Board.Direction.RIGHT : Board.Direction.DOWN;
                Board.PlaceResult res = board.placeShip(x, y, len, dir);
                if (res == Board.PlaceResult.OK) placed = true;
            }
            if (!placed) {
                throw new IllegalStateException("Nie udało się rozmieścić wszystkich statków");
            }
        }
    }

    // @Override
    // public void display() {
    //     setVisible(true);
    // }

    @Override
    public void update(GameEvent event) {
        // simple listener: update UI on events
        SwingUtilities.invokeLater(() -> {
            updateBoards();
            if (event.type != null) {
                System.out.println("Event: " + event.type + " result=" + event.result);
                statusLabel.setText("Event: " + event.type + " result=" + event.result);
            }
        });
    }
    
    @Override    
    public void display() {
        setVisible(true);
    }
}