package gui.gameconfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PvEBoardConfigurator extends JPanel {

    private int size;
    private boolean[][] playerBoard;
    private boolean[][] computerBoard;
    private BoardPanel boardPanel;
    private JPanel shipsPanel;          // <-- dodajemy jako pole klasy
    private JButton confirmButton;
    private Map<Integer, Integer> remainingShips;

    private int selectedShipLength = -1;
    private boolean horizontal = true;

    public PvEBoardConfigurator(int size) {
        this.size = size;
        this.playerBoard = new boolean[size][size];
        this.computerBoard = new boolean[size][size];

        // lista statków
        remainingShips = new LinkedHashMap<>();
        remainingShips.put(4, 1);
        remainingShips.put(3, 2);
        remainingShips.put(2, 3);
        remainingShips.put(1, 4);

        setLayout(new BorderLayout(10,10));

        JLabel title = new JLabel("Ustaw swoje statki", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10,10));

        boardPanel = new BoardPanel(size, 40);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // panel statków jako pole klasy
        shipsPanel = new JPanel();
        shipsPanel.setLayout(new BoxLayout(shipsPanel, BoxLayout.Y_AXIS));
        shipsPanel.setBorder(BorderFactory.createTitledBorder("Pozostałe statki"));
        mainPanel.add(shipsPanel, BorderLayout.EAST);

        updateShipsPanel(); // teraz używamy pola klasy

        add(mainPanel, BorderLayout.CENTER);

        confirmButton = new JButton("Zatwierdź");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> JOptionPane.showMessageDialog(this,"Plansza gotowa!"));
        add(confirmButton, BorderLayout.SOUTH);
    }

    // --- metoda aktualizacji panelu statków ---
    private void updateShipsPanel() {
        shipsPanel.removeAll();
        for (Map.Entry<Integer,Integer> entry: remainingShips.entrySet()) {
            int length = entry.getKey();
            int count = entry.getValue();
            JButton shipButton = new JButton(length + " dł (" + count + " szt.)");
            shipButton.setEnabled(count>0);
            shipButton.addActionListener(e -> selectedShipLength = length);
            shipsPanel.add(shipButton);
        }
        shipsPanel.revalidate();
        shipsPanel.repaint();
    }

    // --- Panel planszy ---
    private class BoardPanel extends JPanel {
        private int cellSize;
        private JButton[][] cells;

        public BoardPanel(int size, int cellSize) {
            this.cellSize = cellSize;
            this.cells = new JButton[size][size];
            setLayout(new GridLayout(size,size,1,1));
            setPreferredSize(new Dimension(size*cellSize,size*cellSize));
            setBackground(Color.CYAN);

            for(int i=0;i<size;i++) {
                for(int j=0;j<size;j++) {
                    JButton cell = new JButton();
                    cell.setBackground(Color.CYAN);
                    cell.setMargin(new Insets(0,0,0,0));
                    cell.setFocusPainted(false);
                    int x=i, y=j;

                    cell.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if(SwingUtilities.isRightMouseButton(e)) {
                                horizontal = !horizontal;
                            } else if(selectedShipLength>0) {
                                placeShip(x,y,selectedShipLength,horizontal);
                            }
                        }
                    });

                    cells[i][j] = cell;
                    add(cell);
                }
            }
        }

        private void placeShip(int x,int y,int length,boolean horizontal) {
            if(!canPlaceShip(x,y,length,horizontal)) {
                JOptionPane.showMessageDialog(this,"Nie można postawić statku tutaj!");
                return;
            }
            for(int i=0;i<length;i++) {
                int xi = horizontal ? x+i : x;
                int yi = horizontal ? y : y+i;
                playerBoard[xi][yi] = true;
                cells[xi][yi].setBackground(Color.GRAY);
            }
            remainingShips.put(length, remainingShips.get(length)-1);
            selectedShipLength = -1;
            confirmButton.setEnabled(remainingShips.values().stream().allMatch(v->v==0));
            updateShipsPanel(); // aktualizacja panelu statków
        }

        private boolean canPlaceShip(int x,int y,int length,boolean horizontal) {
            if(horizontal) {
                if(x+length>size) return false;
                for(int i=0;i<length;i++) if(playerBoard[x+i][y]) return false;
            } else {
                if(y+length>size) return false;
                for(int i=0;i<length;i++) if(playerBoard[x][y+i]) return false;
            }
            return true;
        }
    }

    public boolean[][] getPlayerBoard() { return playerBoard; }
    public boolean[][] getComputerBoard() { return computerBoard; }
}
