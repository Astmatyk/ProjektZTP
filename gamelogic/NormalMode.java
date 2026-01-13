package gamelogic;

import java.util.*;
import gamelogic.enums.MapFlags;

public class NormalMode implements ShootingStrategy {

    private Random rand = new Random();

    @Override
    public Coordinates chooseCoordinates(Board shootingBoard) {

        int size = shootingBoard.getSize();

        // losowanie fifty-fifty czy ma losowac czy sie starac
        if (rand.nextBoolean()) {
            // szuka ostatniego trafienia
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (shootingBoard.getFlag(x, y) == MapFlags.SHIP_WRECKED) {
                        // sprawdza
                        List<Coordinates> neighbors = getNeighbors(x, y, size);
                        Collections.shuffle(neighbors);
                        for (Coordinates n : neighbors) {
                            if (shootingBoard.getFlag(n.x, n.y) == MapFlags.NOTHING) {
                                return n;
                            }
                        }
                    }
                }
            }
        }
        return randomShot(shootingBoard);
    }

    private List<Coordinates> getNeighbors(int x, int y, int size) {
        List<Coordinates> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(new Coordinates(x - 1, y));
        if (x < size - 1) neighbors.add(new Coordinates(x + 1, y));
        if (y > 0) neighbors.add(new Coordinates(x, y - 1));
        if (y < size - 1) neighbors.add(new Coordinates(x, y + 1));
        return neighbors;
    }

    private Coordinates randomShot(Board shootingBoard) {
        int size = shootingBoard.getSize();
        while (true) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (shootingBoard.getFlag(x, y) == MapFlags.NOTHING)
                return new Coordinates(x, y);
        }
    }
}
