package gui.gamePanel;

import gamelogic.*;
import gamelogic.enums.MapFlags;
import gamelogic.enums.ShotResult;
import gui.MainGUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import javax.swing.*;

public class ConGamePanel extends JPanel implements GameListener {

    private final MainGUI mainGUI;
    private JPanel ownBoardPanel;
    private JPanel oppBoardPanel;
    private JButton[][] ownButtons;
    private JButton[][] oppButtons;
    private JLabel statusLabel;

    private Game game;
    private Player humanPlayer;
    private Player opponent;
    private boolean isPvE = true;
    private int mapSize = 10;

    public ConGamePanel(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        setLayout(new BorderLayout(8,8));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] modes = new String[]{"PvE","PvP","EvE"};
        JComboBox<String> modeBox = new JComboBox<>(modes);
        modeBox.addActionListener(e -> isPvE = modeBox.getSelectedItem().equals("PvE"));

        Integer[] sizes = new Integer[]{8,10,12};
        JComboBox<Integer> sizeBox = new JComboBox<>(sizes);
        sizeBox.setSelectedItem(mapSize);
        sizeBox.addActionListener(e -> mapSize = (Integer)sizeBox.getSelectedItem());

        JButton startBtn = new JButton("Nowa gra");
        startBtn.addActionListener(e -> startGame());

        JButton backButton = new JButton("Wstecz");
        backButton.addActionListener(e -> mainGUI.showView("MENU"));

        topPanel.add(new JLabel("Tryb:"));
        topPanel.add(modeBox);
        topPanel.add(new JLabel("Rozmiar:"));
        topPanel.add(sizeBox);
        topPanel.add(startBtn);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1,2,10,10));
        ownBoardPanel = new JPanel();
        oppBoardPanel = new JPanel();
        center.add(wrapWithTitled("Twoja plansza", ownBoardPanel));
        center.add(wrapWithTitled("Plansza przeciwnika", oppBoardPanel));
        add(center, BorderLayout.CENTER);

        statusLabel = new JLabel("Brak aktywnej gry");
        add(statusLabel, BorderLayout.SOUTH);
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

        humanPlayer = new PcPlayer(b1, new Board(mapSize), new HardMode());
        if (isPvE) {
            opponent = new PcPlayer(b2, new Board(mapSize), new HardMode());
        } else {
            opponent = new HumanPlayer(b2, new Board(mapSize), "Player2");
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
        Timer t = new Timer(300, null);
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
        t.setInitialDelay(200);
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
}
