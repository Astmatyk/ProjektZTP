package gamelogic;

public interface ShootingStrategy {
    Coordinates chooseCoordinates(Board shootingBoard);
}
