package gamelogic;

public class PcPlayer extends Player {
    ShootingStrategy strategy;

    public PcPlayer(Board ownBoard, Board shootingBoard, ShootingStrategy strategy) {
        super(ownBoard, shootingBoard);
        this.strategy = strategy;
    }
}
