package gui.gamePanel;

import gamelogic.*;
import gamelogic.enums.*;
import gui.MainGUI;
import java.awt.*;
import javax.swing.*;

public class ConGamePanel extends JPanel implements GamePanelInterface, GameListener {

    private final MainGUI mainGUI;
    private final int mapSize;
    private final int CELL_SIZE = 30;

    private JButton[][] ownButtons;
    private JButton[][] oppButtons;
    private JLabel statusLabel;

    private Game game;
    private Player localPlayer; // Gracz, którego planszę "własną" wyświetlamy po lewej

    public ConGamePanel(MainGUI mainGUI, int size, String p1Name, String p2Name) {
        this.mainGUI = mainGUI;
        this.mapSize = size;
        this.ownButtons = new JButton[size][size];
        this.oppButtons = new JButton[size][size];

        setLayout(new BorderLayout());

        // UI Setup (Statyczne kontenery)
        setupHeader(size);
        setupBoards(p1Name, p2Name);
        setupFooter();
    }

    public void bindGame(Game game) {
        this.game = game;
        this.localPlayer = game.getCurrentPlayer();
        this.game.addListener(this);
        
        updateUIState();
    }

    private void handleShotRequest(int x, int y) {
        if (game == null || game.isGameOver()) return;

        // Jeśli to nie tura gracza przypisanego do tego widoku - ignoruj
        if (game.getCurrentPlayer() != localPlayer) return;

        try {
            // Po prostu wysyłamy informację do silnika
            game.shoot(localPlayer, new Coordinates(x, y));
        } catch (Exception ex) {
            statusLabel.setText("Błąd: " + ex.getMessage());
        }
    }

    private void updateUIState() {
        if (game == null || localPlayer == null) return;

        // 1. Pobierz stan plansz z modelu i odśwież kolory
        Board own = localPlayer.getOwnBoard();
        Board shoot = localPlayer.getShootingBoard();

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                setBtnColor(ownButtons[x][y], own.getFlag(x, y), true);
                setBtnColor(oppButtons[x][y], shoot.getFlag(x, y), false);
            }
        }
    }

    private void setBtnColor(JButton btn, MapFlags flag, boolean isOwn) {
        switch (flag) {
            case NOTHING -> btn.setBackground(Color.CYAN);
            case SHIP -> btn.setBackground(isOwn ? Color.GRAY : Color.CYAN);
            case SHIP_WRECKED -> btn.setBackground(Color.RED);
            case NO_SHIP -> btn.setBackground(Color.BLACK);
        }
    }

    @Override
    public void update(GameEvent event) {
        // Silnik gry wysłał info, że coś się zmieniło (strzał lub koniec)
        SwingUtilities.invokeLater(() -> {
            updateUIState();
            
            if (event.type == EventType.GAME_END) {
                statusLabel.setText("KONIEC!");
            }
        });
    }

    // --- BUDOWA WIDOKU ---

    private void setupHeader(int size) {
        JPanel header = new JPanel(new GridLayout(2, 1));
        JLabel title = new JLabel("Bitwa morska: " + size + "x" + size, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        statusLabel = new JLabel("Inicjalizacja...", SwingConstants.CENTER);
        header.add(title);
        header.add(statusLabel);
        add(header, BorderLayout.NORTH);
    }

    private void setupBoards(String p1, String p2) {
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        gbc.gridx = 0;
        container.add(createLabeledGrid(p1, ownButtons, false), gbc);
        gbc.gridx = 1;
        container.add(createLabeledGrid(p2, oppButtons, true), gbc);

        add(container, BorderLayout.CENTER);
    }

    private JPanel createLabeledGrid(String title, JButton[][] matrix, boolean clickable) {
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

    private void setupFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton menuBtn = new JButton("Menu");
        menuBtn.addActionListener(e -> mainGUI.showView("MENU"));
        footer.add(menuBtn);
        add(footer, BorderLayout.SOUTH);
    }

    @Override public void display() { setVisible(true); }
}