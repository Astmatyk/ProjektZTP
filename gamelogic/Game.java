package gamelogic;


import gamelogic.enums.EventType;
import gamelogic.enums.ShotResult;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player nextPlayer;
    private boolean gameOver = false;
    private GameHistory history;
    private final String gameId;
    private List<GameListener> listenersList = new ArrayList<>();

    public Game(Player p1, Player p2) {
        this(p1, p2, String.valueOf(System.currentTimeMillis()));
    }

    public Game(Player p1, Player p2, String gameId) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = p1;
        this.nextPlayer = p2;
        this.gameId = gameId != null ? gameId : String.valueOf(System.currentTimeMillis());
        this.history = new GameHistory(this.gameId);
    }

    public Player getOpponent(Player p) {
        return (p == player1) ? player2 : player1;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void notify(GameEvent event) {
        for(GameListener listener: listenersList)
        {
            listener.update(event);
        }
    }

    public ShotResult shoot(Player attacker, Coordinates coords) {
        if (gameOver || attacker != currentPlayer) {
            throw new IllegalStateException("Not your turn or game over");
        }

        Player defender = getOpponent(attacker);

        // Board obrońcy przyjmuje strzał
        ShotResult result = defender.receiveShot(coords);

        // Strzelający aktualizuje swoją mapę strzałów
        attacker.updateShootingBoard(coords, result);

        notify(new GameEvent(EventType.SHOT_FIRED,(result!=ShotResult.MISS), attacker));

        // Sprawdzenie końca gry
        if (defender.getOwnBoard().isAllShipsSink()) {
            notify(new GameEvent(EventType.GAME_END,true ,attacker));
            gameOver = true;
        } else {
            // zmiana tury tylko jeśli pudło
            if (result == ShotResult.MISS) {
                currentPlayer = defender;
                nextPlayer = attacker;
            }
        }

        // po każdym strzale następuje save naszego stanu
        save(result);

        return result;
    }

    public Snapshot save(ShotResult result) {
        Snapshot snap = new Snapshot(player1, player2, currentPlayer, result);
        history.push(snap);
        return snap;
    }

    public void restore(Snapshot memento) {
        if (memento == null) throw new IllegalArgumentException("memento == null");

        // zamieniamy graczy na tych ze snapshota
        this.player1 = memento.getP1Board();
        this.player2 = memento.getP2Board();
        this.currentPlayer = memento.getCurrentPlayer();

        // trzeba też sprawdzić gameOver (to fajnie by było dać do osobnej funkcji)
        this.gameOver = player1.getOwnBoard().isAllShipsSink() || player2.getOwnBoard().isAllShipsSink();
    }

    public GameHistory getHistory() {
        return history;
    }

    public void addListener(GameListener listener) {
        listenersList.add(listener);
    }

}
