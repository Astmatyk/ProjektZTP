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

    protected Map<Integer, Integer> remainingShips = new LinkedHashMap<>(); // Inicjalizacja domyślna
    protected int selectedShipLength = -1;
    protected boolean horizontal = true;

    public BoardConfiguratorAbstract(int size) {
        this.size = size;
        this.player1Board = new boolean[size][size];
        this.player2Board = new boolean[size][size];
    }

    // Klasy potomne muszą powiedzieć, którą planszę teraz "klika" gracz
    protected abstract boolean[][] getActiveBoard();

    // --- LOGIKA STAWIANIA ---
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

    protected void placeShip(boolean[][] board, int x, int y, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int xi = horizontal ? x + i : x;
            int yi = horizontal ? y : y + i;
            board[xi][yi] = true;
        }
    }

    protected boolean[][] placeRandomFleet(int size) {
        int[] ships = new int[]{4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        boolean[][] board = new boolean[size][size];
        Random rnd = new Random();
        for (int len : ships) {
            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < 500) {
                attempts++;
                int x = rnd.nextInt(size);
                int y = rnd.nextInt(size);
                boolean hor = rnd.nextBoolean();
                if (canPlaceShip(board, x, y, len, hor)) {
                    placeShip(board, x, y, len, hor);
                    placed = true;
                }
            }
        }
        return board;
    }

    // --- GUI ---
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

    protected class BoardPanel extends JPanel {
        private final JButton[][] cells = new JButton[size][size];
        private int hoverX = -1, hoverY = -1;

        BoardPanel() {
            setLayout(new GridLayout(size, size, 1, 1));
            setBackground(Color.BLACK);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    JButton cell = new JButton();
                    cell.setBackground(Color.CYAN);
                    cell.setPreferredSize(new Dimension(40, 40));
                    final int fx = x, fy = y;
                    cell.addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e) { hoverX = fx; hoverY = fy; repaintPreview(); }
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) { horizontal = !horizontal; repaintPreview(); }
                            else if (selectedShipLength > 0 && canPlaceShip(getActiveBoard(), fx, fy, selectedShipLength, horizontal)) {
                                placeShip(getActiveBoard(), fx, fy, selectedShipLength, horizontal);
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

        public void clearVisuals() {
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++) cells[x][y].setBackground(Color.CYAN);
        }

        private void repaintPreview() {
            clearPreview();
            if (selectedShipLength <= 0 || hoverX < 0) return;
            boolean ok = canPlaceShip(getActiveBoard(), hoverX, hoverY, selectedShipLength, horizontal);
            Color color = ok ? new Color(0, 200, 0, 150) : new Color(200, 0, 0, 150);
            for (int i = 0; i < selectedShipLength; i++) {
                int nx = horizontal ? hoverX + i : hoverX;
                int ny = horizontal ? hoverY : hoverY + i;
                if (nx < size && ny < size) cells[nx][ny].setBackground(color);
            }
        }

        private void clearPreview() {
            boolean[][] b = getActiveBoard();
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++) cells[x][y].setBackground(b[x][y] ? Color.GRAY : Color.CYAN);
        }
    }

    public java.util.List<boolean[][]> getBoards() {
        return Arrays.asList(player1Board, player2Board);
    }
}