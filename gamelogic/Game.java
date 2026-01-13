package gamelogic;

import gamelogic.enums.ShotResult;

public class Game {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean gameOver = false;

    public Game(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = p1;
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

    public ShotResult shoot(Player attacker, Coordinates coords) {
        if (gameOver || attacker != currentPlayer) {
            throw new IllegalStateException("Not your turn or game over");
        }

        Player defender = getOpponent(attacker);

        // Board obrońcy przyjmuje strzał
        ShotResult result = defender.receiveShot(coords);

        // Strzelający aktualizuje swoją mapę strzałów
        attacker.updateShootingBoard(coords, result);

        // Sprawdzenie końca gry
        if (defender.getOwnBoard().isAllShipsSink()) {
            gameOver = true;
        } else {
            // zmiana tury tylko jeśli pudło
            if (result == ShotResult.MISS) {
                currentPlayer = defender;
            }
        }

        return result;
    }
}
