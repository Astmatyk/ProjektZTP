package gamelogic;

import java.io.Serializable;

public interface ShootingStrategy extends Serializable {
    Coordinates chooseCoordinates(Board shootingBoard);
}
