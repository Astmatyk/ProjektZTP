package gamelogic;

import java.util.*;
import gamelogic.enums.MapFlags;

public class HardMode implements ShootingStrategy {

    private Random rand = new Random();

    @Override
    public Coordinates chooseCoordinates(Board board) {
        int size = board.getSize();
  
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board.getFlag(x, y) == MapFlags.SHIP_WRECKED) {
                    // szukaj NOTHING (nie zestrzelone) w poblizu
                    List<Coordinates> neighbors = getNeighbors(x, y, size);
                    for (Coordinates n : neighbors) {
                        if (board.getFlag(n.x, n.y) == MapFlags.NOTHING)
                            return n;
                    }
                }
            }
        }
        return randomShot(board);
    }

    private List<Coordinates> getNeighbors(int x, int y, int size) {
        List<Coordinates> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(new Coordinates(x - 1, y));
        if (x < size - 1) neighbors.add(new Coordinates(x + 1, y));
        if (y > 0) neighbors.add(new Coordinates(x, y - 1));
        if (y < size - 1) neighbors.add(new Coordinates(x, y + 1));
        return neighbors;
    }
    
    
    private Coordinates randomShot(Board board) {
        int size = board.getSize();
        while (true) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (board.getFlag(x, y) == MapFlags.NOTHING)
                return new Coordinates(x, y);
        }
    }
}