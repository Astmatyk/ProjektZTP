package gamelogic;
import java.util.Random;

import gamelogic.enums.MapFlags;

public class EasyMode implements ShootingStrategy {
    @Override
    public Coordinates chooseCoordinates(Board shootingBoard) {
    	Random rand=new Random();
    	
    	int boardSize=shootingBoard.getSize();
        while (true) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);

            if (shootingBoard.getFlag(x, y) == MapFlags.NOTHING) {
                return new Coordinates(x, y);
            }
        }
    }
}
