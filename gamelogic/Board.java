package gamelogic;
import gamelogic.enums.*;

public class Board {
    private int size;
    private MapFlags[][] cells;

    public Board(int size) {
        this.size = size;
        this.cells = new MapFlags[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cells[y][x] = MapFlags.NOTHING;
            }
        }
    }

    public void setFlag(MapFlags flag, int x, int y) {
        cells[y][x] = flag;
    }

    public void placeShip() {

    }

    public boolean isAllShipsSink() {
        return false;
    }
}
