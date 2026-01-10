package gamelogic;

import java.util.*;
import gamelogic.enums.MapFlags;

public class HardMode implements ShootingStrategy {

    private Random rand = new Random();

    @Override
    public Coordinates chooseCoordinates(Board board) {
        List<Coordinates> hits = board.getAllHits();

        if (!hits.isEmpty()) {
            for (Coordinates h : hits) {
                for (Coordinates n : board.getNeighbors(h)) {
                    if (board.getFlag(n.x, n.y) == MapFlags.NOTHING)
                        return n;
                }
            }
        }

        return randomShot(board);
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