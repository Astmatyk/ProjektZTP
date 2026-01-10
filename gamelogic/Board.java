package gamelogic;
import java.util.ArrayList;

import java.util.List;

import gamelogic.enums.*;
import gamelogic.enums.ShotResult;

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

    public int getSize(){
        return size;
    }

    public void setFlag(MapFlags flag, int x, int y) {
        cells[y][x] = flag;
    }

    public MapFlags getFlag(int x, int y) {
        return cells[y][x];
    }
    
    public List<Coordinates> getAllHits() {
        List<Coordinates> hits = new ArrayList<>();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (cells[y][x] == MapFlags.HIT) {
                    hits.add(new Coordinates(x, y));
                }
            }
        }
        return hits;
    }
    
    public List<Coordinates> getNeighbors(Coordinates c) {
        List<Coordinates> list = new ArrayList<>();

        int x = c.x;
        int y = c.y;

        if (x > 0) list.add(new Coordinates(x - 1, y));
        if (x < size - 1) list.add(new Coordinates(x + 1, y));
        if (y > 0) list.add(new Coordinates(x, y - 1));
        if (y < size - 1) list.add(new Coordinates(x, y + 1));

        return list;
    }
    
    public ShotResult receiveShot(Coordinates c) {
        int x = c.x;
        int y = c.y;

        MapFlags field = cells[y][x];

        switch (field) {
            case NOTHING:
                cells[y][x] = MapFlags.MISS;
                return ShotResult.MISS;

            case SHIP:
                cells[y][x] = MapFlags.HIT;
                return ShotResult.HIT;   

            case HIT:
            case MISS:
                return ShotResult.MISS;

            default:
                throw new IllegalStateException("Unknown field state");
        }
    }
    
    public void placeShip() {

    }

    public boolean isAllShipsSink() {
        return false;
    }
}
