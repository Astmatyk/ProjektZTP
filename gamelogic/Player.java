package gamelogic;

import gamelogic.enums.MapFlags;
import gamelogic.enums.ShotResult;
import java.io.Serializable;

public abstract class Player implements Serializable {
    protected Board ownBoard;
    protected Board shootingBoard;

    public Player(Board ownBoard, Board shootingBoard) {
        this.ownBoard = ownBoard;
        this.shootingBoard = shootingBoard;
    }

    public abstract Coordinates chooseCoordinates();

    // Gracz otrzymuje strzal od przeciwnika
    public ShotResult receiveShot(Coordinates coords) {
        return ownBoard.shoot(coords.x, coords.y);
    }

    // Aktualizacja własnego shootingBoard na podstawie wyniku strzalu
    public void updateShootingBoard(Coordinates coords, ShotResult result) {
        switch (result) {

            case MISS -> shootingBoard.setFlag(MapFlags.NO_SHIP, coords.x, coords.y);
            case HIT -> shootingBoard.setFlag(MapFlags.SHIP_WRECKED, coords.x, coords.y);
            case SINK -> shootingBoard.markSunkShip(coords.x, coords.y);
        }
    }

    public Board getOwnBoard() {
        return ownBoard;
    }

    public Board getShootingBoard() {
        return shootingBoard;
    }
}
