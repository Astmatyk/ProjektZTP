package gamelogic;

import gamelogic.enums.MapFlags;
import gamelogic.enums.ShotResult;

public abstract class Player {

    protected Board ownBoard;
    protected Board shootingBoard;

    public Player(Board ownBoard, Board shootingBoard) {
        this.ownBoard = ownBoard;
        this.shootingBoard = shootingBoard;
    }

    public abstract Coordinates chooseCoordinates();

    public ShotResult receiveShot(Coordinates coords) {
        return ownBoard.receiveShot(coords);
    }

    public void updateShootingBoard(Coordinates coords, ShotResult result) {
        if (result == ShotResult.MISS)
            shootingBoard.setFlag(MapFlags.MISS, coords.x, coords.y);
        else
            shootingBoard.setFlag(MapFlags.HIT, coords.x, coords.y);
    }

    public Board getOwnBoard() {
        return ownBoard;
    }

    public Board getShootingBoard() {
        return shootingBoard;
    }
}