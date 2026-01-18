package gui.gamePanel;

import gamelogic.*;
import gamelogic.enums.*;
import javax.swing.Timer;

/**
 * Dekorator EvE z automatycznym sterowaniem botami.
 */
public class EvEGamePanel extends GamePanelDecorator implements GameListener {

    private Player bot1;
    private Player bot2;
    private Timer gameLoop; // Mechanizm napędzający ruchy botów
    
    ConGamePanel base = (ConGamePanel) wrappedPanel;
    
    public EvEGamePanel(GamePanelInterface panel) {
        super(panel);
    }

    @Override
    public void display() {
        wrappedPanel.display();
    }

    @Override
    public void bindGame(Game game) {
        wrappedPanel.bindGame(game);
        
        // Inicjalizacja botów
        this.bot1 = game.getCurrentPlayer();
        this.bot2 = game.getOpponent(bot1);
        
        // Tworzymy Timer, który co 500ms (pół sekundy) zmusi bota do ruchu
        gameLoop = new Timer(500, e -> triggerBotMove());
        gameLoop.start();

        updateUIWithFullVisibility();
    }

    /**
     * Metoda wymuszająca ruch aktualnego bota.
     */
    private void triggerBotMove() {
        Game game = base.game;

        if (game == null || game.isGameOver()) {
            gameLoop.stop();
            return;
        }

        game.playerTurn();
    }


    @Override
    public void update(GameEvent event) {
        // Przy każdym strzale odświeżamy widok
        updateUIWithFullVisibility();
        
        // Jeśli gra się skończyła, zatrzymujemy pętlę
        if (event.type == EventType.GAME_END) {
            if (gameLoop != null) gameLoop.stop();
        }
    }

    private void updateUIWithFullVisibility() {
        if (!(wrappedPanel instanceof ConGamePanel)) return;
        ConGamePanel base = (ConGamePanel) wrappedPanel;
        
        if (bot1 == null || bot2 == null) return;

        // Rysujemy stan obu plansz własnych
        for (int y = 0; y < base.mapSize; y++) {
            for (int x = 0; x < base.mapSize; x++) {
                base.setBtnColor(base.ownButtons[x][y], bot1.getOwnBoard(), x, y, false);
                base.setBtnColor(base.oppButtons[x][y], bot2.getOwnBoard(), x, y, false);
            }
        }
        base.updateStatusLabel();
    }
}