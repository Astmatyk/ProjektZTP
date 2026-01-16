package gui.gameconfig;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class BoardConfigurator extends JPanel {

    private final int size;
    private final boolean[][] playerBoard;
    private final boolean[][] computerBoard;

    private BoardPanel boardPanel;
    private JPanel shipsPanel;
    private JButton confirmButton;

    private Map<Integer, Integer> remainingShips;

    private int selectedShipLength = -1;
    private boolean horizontal = true;

    public BoardConfigurator(int size) {
        this.size = size;
        this.playerBoard = new boolean[size][size];
        this.computerBoard = new boolean[size][size];

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

        boardPanel = new BoardPanel(size);
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

    private void updateShipsPanel() {
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

    private class BoardPanel extends JPanel {

        private final int cellSize = 40;
        private final JButton[][] cells;

        private int hoverX = -1;
        private int hoverY = -1;

        public BoardPanel(int size) {
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
                            } else if (SwingUtilities.isLeftMouseButton(e) && selectedShipLength > 0) {
                                if (canPlaceShip(fx, fy, selectedShipLength, horizontal)) {
                                    placeShip(fx, fy, selectedShipLength, horizontal);
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

            boolean ok = canPlaceShip(hoverX, hoverY, selectedShipLength, horizontal);
            Color color = ok ? new Color(0, 200, 0, 120) : new Color(200, 0, 0, 120);

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
                    if (!playerBoard[x][y]) {
                        cells[x][y].setBackground(Color.CYAN);
                    }
                }
            }
        }

        private void placeShip(int x, int y, int length, boolean horizontal) {
            for (int i = 0; i < length; i++) {
                int xi = horizontal ? x + i : x;
                int yi = horizontal ? y : y + i;
                playerBoard[xi][yi] = true;
                cells[xi][yi].setBackground(Color.GRAY);
            }

            remainingShips.put(length, remainingShips.get(length) - 1);
            selectedShipLength = -1;
            updateShipsPanel();

            confirmButton.setEnabled(
                    remainingShips.values().stream().allMatch(v -> v == 0)
            );
        }

        private boolean canPlaceShip(int x, int y, int length, boolean horizontal) {
            for (int i = 0; i < length; i++) {
                int xi = horizontal ? x + i : x;
                int yi = horizontal ? y : y + i;

                if (xi < 0 || yi < 0 || xi >= size || yi >= size)
                    return false;

                if (playerBoard[xi][yi])
                    return false;

                // 8 kierunków
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int nx = xi + dx;
                        int ny = yi + dy;
                        if (nx >= 0 && ny >= 0 && nx < size && ny < size) {
                            if (playerBoard[nx][ny]) return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    public boolean[][] getPlayerBoard() {
        return playerBoard;
    }

    public boolean[][] getComputerBoard() {
        return computerBoard;
    }
}