package gamelogic;

public class PcPlayer extends Player {

    private ShootingStrategy strategy;

    public PcPlayer(Board ownBoard, Board shootingBoard, ShootingStrategy strategy) {
        super(ownBoard, shootingBoard);
        this.strategy = strategy;
    }

    @Override
    public Coordinates chooseCoordinates() {
        return strategy.chooseCoordinates(shootingBoard);
    }
}