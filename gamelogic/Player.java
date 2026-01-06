package gamelogic;

public class Player {
    private Board ownBoard;
    private Board shootingBoard;
    private ShotResult shotResult;
    private ShootingStrategy strategy;

    public Player(Board ownBoard, Board shootingBoard) {
        this.ownBoard = ownBoard;
        this.shootingBoard = shootingBoard;
    }
}
