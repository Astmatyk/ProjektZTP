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

    protected Map<Integer, Integer> remainingShips;

    protected int selectedShipLength = -1;
    protected boolean horizontal = true;

    public BoardConfiguratorAbstract(int size) {
        this.size = size;
        this.player1Board = new boolean[size][size];
    }

    /* ======================================================
       =============== WSPÓLNA LOGIKA GRY ===================
       ====================================================== */

    protected boolean canPlaceShip(boolean[][] board, int x, int y, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int xi = horizontal ? x + i : x;
            int yi = horizontal ? y : y + i;

            if (xi < 0 || yi < 0 || xi >= size || yi >= size)
                return false;

            if (board[xi][yi])
                return false;

            // 8 kierunków (zakaz stykania)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = xi + dx;
                    int ny = yi + dy;
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

    /* ======================================================
       ================= LOSOWANIE FLOTY ====================
       ====================================================== */

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
                boolean horizontal = rnd.nextBoolean();

                if (canPlaceShip(board, x, y, len, horizontal)) {
                    placeShip(board, x, y, len, horizontal);
                    placed = true;
                }
            }

            if (!placed) {
                throw new IllegalStateException("Nie udało się rozmieścić wszystkich statków");
            }
        }
        return board;
    }

    /* ======================================================
       ======================= GUI ==========================
       ====================================================== */

    protected void updateShipsPanel() {
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

        private final int cellSize = 40;
        private final JButton[][] cells;

        private int hoverX = -1;
        private int hoverY = -1;

        BoardPanel() {
            cells = new JButton[size][size];
            setLayout(new GridLayout(size, size, 1, 1));
            setPreferredSize(new Dimension(size * cellSize, size * cellSize));
            setBackground(Color.BLACK);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    JButton cell = new JButton();
                    cell.setBackground(Color.CYAN);
                    cell.setMargin(new Insets(0, 0, 0, 0));
                    cell.setFocusPainted(false);

                    final int fx = x;
                    final int fy = y;

                    cell.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            hoverX = fx;
                            hoverY = fy;
                            repaintPreview();
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                horizontal = !horizontal;
                                repaintPreview();
                            } else if (SwingUtilities.isLeftMouseButton(e)
                                    && selectedShipLength > 0) {

                                if (canPlaceShip(player1Board, fx, fy,
                                        selectedShipLength, horizontal)) {
                                    placeShipGui(fx, fy, selectedShipLength, horizontal);
                                    clearPreview();
                                }
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

            boolean ok = canPlaceShip(player1Board, hoverX, hoverY,
                    selectedShipLength, horizontal);

            Color color = ok
                    ? new Color(0, 200, 0, 120)
                    : new Color(200, 0, 0, 120);

            for (int i = 0; i < selectedShipLength; i++) {
                int x = horizontal ? hoverX + i : hoverX;
                int y = horizontal ? hoverY : hoverY + i;
                if (x < size && y < size) {
                    cells[x][y].setBackground(color);
                }
            }
        }

        private void clearPreview() {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    cells[x][y].setBackground(
                            player1Board[x][y] ? Color.GRAY : Color.CYAN);
                }
            }
        }

        private void placeShipGui(int x, int y, int length, boolean horizontal) {
            placeShip(player1Board, x, y, length, horizontal);

            for (int i = 0; i < length; i++) {
                int xi = horizontal ? x + i : x;
                int yi = horizontal ? y : y + i;
                cells[xi][yi].setBackground(Color.GRAY);
            }

            remainingShips.put(length, remainingShips.get(length) - 1);
            selectedShipLength = -1;
            updateShipsPanel();

            confirmButton.setEnabled(
                    remainingShips.values().stream().allMatch(v -> v == 0)
            );
        }
    }

    /* ======================================================
       ======================= API ==========================
       ====================================================== */

    public java.util.List<boolean[][]> getBoards() {
        java.util.List<boolean[][]> boards = new ArrayList<>(2);
        boards.add(player1Board);
        boards.add(player2Board);
        return boards;
    }
}
