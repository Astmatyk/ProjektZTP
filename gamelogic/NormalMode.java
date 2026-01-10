package gamelogic;

import java.util.*;
import gamelogic.enums.MapFlags;

public class NormalMode implements ShootingStrategy {

    private Random rand = new Random();

    @Override
    public Coordinates chooseCoordinates(Board board) {

        List<Coordinates> hits = board.getAllHits();

        // Jeśli mamy trafienia i los < 50% → dobijamy
        if (!hits.isEmpty() && rand.nextBoolean()) {
            Coordinates base = hits.get(hits.size() - 1);
            List<Coordinates> neighbors = board.getNeighbors(base);

            Collections.shuffle(neighbors);
            for (Coordinates c : neighbors) {
                if (board.getFlag(c.x, c.y) == MapFlags.NOTHING)
                    return c;
            }
        }

        // fallback → losowo
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