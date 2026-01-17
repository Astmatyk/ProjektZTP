package gui.gamePanel;

import gamelogic.*;
import gamelogic.enums.*;
import gui.MainGUI;
import java.awt.*;
import javax.swing.*;

/**
 * Bazowy panel gry. Odpowiada za strukturę UI i komunikację z modelem.
 * Dzięki modyfikatorom protected i metodom pomocniczym, dekoratory mogą 
 * łatwo zmieniać perspektywę wyświetlania (kto co widzi).
 */
public class ConGamePanel extends JPanel implements GamePanelInterface, GameListener {

    protected final MainGUI mainGUI;
    protected final int mapSize;
    protected final int CELL_SIZE = 30;

    protected JButton[][] ownButtons;
    protected JButton[][] oppButtons;
    protected JLabel statusLabel;

    protected Game game;
    protected Player localPlayer; // Gracz, którego perspektywę prezentuje ten panel

    public ConGamePanel(MainGUI mainGUI, int size, String p1Name, String p2Name) {
        this.mainGUI = mainGUI;
        this.mapSize = size;
        this.ownButtons = new JButton[size][size];
        this.oppButtons = new JButton[size][size];

        setLayout(new BorderLayout());

        // UI Setup
        setupHeader(size);
        setupBoards(p1Name, p2Name);
        setupFooter();
    }

    @Override
    public void display() {
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void bindGame(Game game) {
        this.game = game;
        // Domyślnie ustawiamy localPlayer na gracza, który zaczyna w modelu
        this.localPlayer = game.getCurrentPlayer();
        this.game.addListener(this);
        
        updateUIState();
    }

    /**
     * Główna metoda odświeżająca interfejs.
     */
    public void updateUIState() {
        if (game == null || localPlayer == null) return;

        // Pobieramy logiczne plansze do wyświetlenia przez hooki dla dekoratorów
        Board leftBoard = getBoardForDisplay(false); 
        Board rightBoard = getBoardForDisplay(true);

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                setBtnColor(ownButtons[x][y], leftBoard, x, y, false);
                setBtnColor(oppButtons[x][y], rightBoard, x, y, true);
            }
        }
        
        updateStatusLabel();
    }

    /**
     * Określa, którą planszę z modelu pobrać dla danej strony widoku.
     */
    protected Board getBoardForDisplay(boolean isOpponentPane) {
        if (isOpponentPane) {
            return localPlayer.getShootingBoard();
        } else {
            return localPlayer.getOwnBoard();
        }
    }

    /**
     * Logika kolorowania przycisków na podstawie flag z Board.
     */
    protected void setBtnColor(JButton btn, Board board, int x, int y, boolean isOpponentBoard) {
        MapFlags flag = board.getFlag(x, y);
        
        switch (flag) {
            case SHIP:
                // Na planszy przeciwnika statki są ukryte (turkusowe)
                btn.setBackground(isOpponentBoard ? Color.CYAN : Color.GRAY);
                break;
            case SHIP_WRECKED: // MapFlags z Twojego Player.java
                btn.setBackground(Color.RED);
                break;
            case NO_SHIP: // MapFlags z Twojego Player.java
                btn.setBackground(Color.WHITE);
                break;
            // Dodaję obsługę HIT/MISS/SUNK jeśli Twój Board używa innych enumów niż Player
            default:
                // Domyślny kolor wody
                btn.setBackground(Color.CYAN);
                break;
        }
    }

    protected void updateStatusLabel() {
        if (game == null) return;

        if (game.isGameOver()) {
            // Skoro gra się skończyła, CurrentPlayer w klasie Game to zwycięzca (ostatni strzelający)
            statusLabel.setText("KONIEC GRY! Zwycięzca: " + game.getCurrentPlayer().toString());
        } else {
            statusLabel.setText("Tura gracza: " + game.getCurrentPlayer().toString());
        }
    }

    /**
     * NAPRAWIONE: Obsługuje strzał korzystając z metody shoot() dostępnej w klasie Game.
     */
    protected void handleShotRequest(int x, int y) {
        if (game == null || game.isGameOver()) return;

        // Sprawdzamy czy to tura "naszego" gracza (lokalnego)
        if (game.getCurrentPlayer() == localPlayer) {
            try {
                // Wywołujemy shoot z klasy Game, przekazując aktualnego gracza i koordynaty
                game.shoot(game.getCurrentPlayer(), new Coordinates(x, y));
                // updateUIState() zostanie wywołane przez update(GameEvent)
            } catch (IllegalStateException e) {
                System.err.println("Błąd ruchu: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(GameEvent event) {
        // Każde zdarzenie w grze (strzał, koniec) odświeża widok
        updateUIState();
        
        if (event.type == EventType.GAME_END) {
            JOptionPane.showMessageDialog(this, "Koniec gry!");
        }
    }

    // --- Metody budujące UI (niezmienione strukturalnie) ---

    protected void setupHeader(int size) {
        JPanel header = new JPanel(new GridLayout(2, 1));
        JLabel title = new JLabel("BITWA MORSKA", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        statusLabel = new JLabel("Inicjalizacja...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        header.add(title);
        header.add(statusLabel);
        add(header, BorderLayout.NORTH);
    }

    protected void setupBoards(String p1Name, String p2Name) {
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        gbc.gridx = 0;
        container.add(createLabeledGrid(p1Name + " (Twoja flota)", ownButtons, false), gbc);

        gbc.gridx = 1;
        container.add(createLabeledGrid(p2Name + " (Twoje strzały)", oppButtons, true), gbc);

        add(container, BorderLayout.CENTER);
    }

    protected JPanel createLabeledGrid(String title, JButton[][] matrix, boolean clickable) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel grid = new JPanel(new GridLayout(mapSize, mapSize, 1, 1));
        grid.setBackground(Color.DARK_GRAY);
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                btn.setBackground(Color.CYAN);
                btn.setFocusPainted(false);
                if (clickable) {
                    final int fx = x; final int fy = y;
                    btn.addActionListener(e -> handleShotRequest(fx, fy));
                }
                matrix[x][y] = btn;
                grid.add(btn);
            }
        }
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    protected void setupFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton menuBtn = new JButton("Menu Główne");
        menuBtn.addActionListener(e -> mainGUI.showView("MENU"));
        footer.add(menuBtn);
        add(footer, BorderLayout.SOUTH);
    }

    public void setLocalPlayer(Player player) {
        this.localPlayer = player;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }
}