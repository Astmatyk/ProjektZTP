package gui.gameconfig;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public abstract class BoardConfiguratorAbstract extends JPanel {

    protected final int size;
    protected boolean[][] player1Board;
    protected boolean[][] player2Board;

    protected BoardPanel boardPanel;
    protected JPanel shipsPanel;
    protected JButton confirmButton;
    protected JLabel titleLabel;

    protected Map<Integer, Integer> remainingShips = new LinkedHashMap<>();
    protected int selectedShipLength = -1;
    protected boolean horizontal = true;

    public BoardConfiguratorAbstract(int size) {
        this.size = size;
        this.player1Board = new boolean[size][size];
        this.player2Board = new boolean[size][size];
    }

    /**
     * Główna metoda budująca interfejs dla konkretnej tablicy.
     */
    protected void fillUI(boolean[][] targetBoard, String titleText, String buttonText) {
        this.removeAll();
        this.setLayout(new BorderLayout(10, 10));

        // Nagłówek
        titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // Centrum: Plansza
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        this.boardPanel = new BoardPanel(targetBoard);
        centerPanel.add(boardPanel, BorderLayout.CENTER);

        // Prawa strona: Statki
        this.shipsPanel = new JPanel();
        shipsPanel.setLayout(new BoxLayout(shipsPanel, BoxLayout.Y_AXIS));
        shipsPanel.setBorder(BorderFactory.createTitledBorder("Pozostałe statki"));
        updateShipsPanel();
        centerPanel.add(shipsPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Dół: Przycisk
        this.confirmButton = new JButton(buttonText);
        this.confirmButton.setEnabled(false);
        add(confirmButton, BorderLayout.SOUTH);

        this.revalidate();
        this.repaint();
    }

    // --- LOGIKA POMOCNICZA ---

    protected void initRemainingShips() {
        remainingShips.clear();
        remainingShips.put(4, 1);
        remainingShips.put(3, 2);
        remainingShips.put(2, 3);
        remainingShips.put(1, 4);
    }

    protected boolean canPlaceShip(boolean[][] board, int x, int y, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int xi = horizontal ? x + i : x;
            int yi = horizontal ? y : y + i;

            if (xi < 0 || yi < 0 || xi >= size || yi >= size) return false;
            if (board[xi][yi]) return false;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = xi + dx; int ny = yi + dy;
                    if (nx >= 0 && ny >= 0 && nx < size && ny < size) {
                        if (board[nx][ny]) return false;
                    }
                }
            }
        }
        return true;
    }

    protected void updateShipsPanel() {
        if (shipsPanel == null) return;
        shipsPanel.removeAll();
        for (Map.Entry<Integer, Integer> entry : remainingShips.entrySet()) {
            int length = entry.getKey();
            int count = entry.getValue();
            JButton btn = new JButton(length + " dł (" + count + ")");
            btn.setEnabled(count > 0);
            btn.addActionListener(e -> selectedShipLength = length);
            shipsPanel.add(btn);
        }
        shipsPanel.revalidate();
        shipsPanel.repaint();
    }

    protected boolean[][] placeRandomFleet(int size) {
        boolean[][] board = new boolean[size][size];
        int[] ships = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        Random rnd = new Random();
        for (int len : ships) {
            boolean placed = false;
            while (!placed) {
                int x = rnd.nextInt(size), y = rnd.nextInt(size);
                boolean hor = rnd.nextBoolean();
                if (canPlaceShip(board, x, y, len, hor)) {
                    for (int i = 0; i < len; i++) board[hor ? x + i : x][hor ? y : y + i] = true;
                    placed = true;
                }
            }
        }
        return board;
    }

    // --- KLASA WEWNĘTRZNA PANELU ---

    protected class BoardPanel extends JPanel {
        private final JButton[][] cells = new JButton[size][size];
        private final boolean[][] target;
        private int hoverX = -1, hoverY = -1;

        BoardPanel(boolean[][] target) {
            this.target = target;
            setLayout(new GridLayout(size, size, 1, 1));
            setBackground(Color.BLACK);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    JButton cell = new JButton();
                    cell.setBackground(target[x][y] ? Color.GRAY : Color.CYAN);
                    cell.setPreferredSize(new Dimension(40, 40));
                    final int fx = x, fy = y;
                    
                    cell.addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e) { hoverX = fx; hoverY = fy; repaintPreview(); }
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) { horizontal = !horizontal; repaintPreview(); }
                            else if (selectedShipLength > 0 && canPlaceShip(target, fx, fy, selectedShipLength, horizontal)) {
                                for (int i = 0; i < selectedShipLength; i++) target[horizontal ? fx + i : fx][horizontal ? fy : fy + i] = true;
                                remainingShips.put(selectedShipLength, remainingShips.get(selectedShipLength) - 1);
                                selectedShipLength = -1;
                                updateShipsPanel();
                                clearPreview();
                                confirmButton.setEnabled(remainingShips.values().stream().allMatch(v -> v == 0));
                            }
                        }
                    });
                    cells[x][y] = cell;
                    add(cell);
                }
            }
        }

        private void repaintPreview() {
            clearPreview();
            if (selectedShipLength <= 0 || hoverX < 0) return;
            boolean ok = canPlaceShip(target, hoverX, hoverY, selectedShipLength, horizontal);
            Color color = ok ? new Color(0, 200, 0, 150) : new Color(200, 0, 0, 150);
            for (int i = 0; i < selectedShipLength; i++) {
                int nx = horizontal ? hoverX + i : hoverX;
                int ny = horizontal ? hoverY : hoverY + i;
                if (nx < size && ny < size) cells[nx][ny].setBackground(color);
            }
        }

        private void clearPreview() {
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++) cells[x][y].setBackground(target[x][y] ? Color.GRAY : Color.CYAN);
        }
    }

    public java.util.List<boolean[][]> getBoards() {
        return Arrays.asList(player1Board, player2Board);
    }
}