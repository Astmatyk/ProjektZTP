package gamelogic;

import gamelogic.enums.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import java.util.List;
import java.util.ArrayList;

public class Board implements Serializable {
    private int size;
    private MapFlags[][] cells;

    // Jeśli false: statki nie mogą się stykać (nawet rogami).
    // Jeśli true: dotykanie jest dozwolone.
    private boolean allowTouchingShips = false;

    public Board(int size) {
        if (size <= 0) throw new IllegalArgumentException("size must be > 0");
        this.size = size;
        this.cells = new MapFlags[size][size];

        // Inicjalizacja całej planszy jako pusta woda
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cells[y][x] = MapFlags.NOTHING;
            }
        }
    }

    public enum Direction {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0);

        final int dx, dy;
        Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }
    }

    public enum PlaceResult {
        OK,
        OUT_OF_BOUNDS,
        LENGTH_INVALID,
        COLLIDES,
        TOUCHING_NOT_ALLOWED
    }

    public int getSize() {
        return size;
    }

    public boolean isAllowTouchingShips() {
        return allowTouchingShips;
    }

    // Sprawdza czy współrzędne mieszczą się w planszy.
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public MapFlags getFlag(int x, int y) {
        requireInBounds(x, y);
        return cells[y][x];
    }

    public void setFlag(MapFlags flag, int x, int y) {
        requireInBounds(x, y);
        if (flag == null) throw new IllegalArgumentException("flag is null");
        cells[y][x] = flag;
    }

    public void setAllowTouchingShips(boolean allowTouchingShips) {
        this.allowTouchingShips = allowTouchingShips;
    }
    

    public PlaceResult placeShip(int startX, int startY, int length, Direction dir) {
        if (dir == null) throw new IllegalArgumentException("dir is null");
        if (length <= 0) return PlaceResult.LENGTH_INVALID;

        // Liczymy koniec statku, żeby sprawdzić OUT_OF_BOUNDS jednym warunkiem.
        int endX = startX + (length - 1) * dir.dx;
        int endY = startY + (length - 1) * dir.dy;

        if (!inBounds(startX, startY) || !inBounds(endX, endY)) {
            return PlaceResult.OUT_OF_BOUNDS;
        }

        // walidacja: kolizje + ewentualnie dotykanie
        for (int i = 0; i < length; i++) {
            int x = startX + i * dir.dx;
            int y = startY + i * dir.dy;

            MapFlags f = cells[y][x];
            if (f != MapFlags.NOTHING) {
                return PlaceResult.COLLIDES;
            }

            if (!allowTouchingShips && touchesAnyShip(x, y)) {
                return PlaceResult.TOUCHING_NOT_ALLOWED;
            }
        }

        // ustawienie statku
        for (int i = 0; i < length; i++) {
            int x = startX + i * dir.dx;
            int y = startY + i * dir.dy;
            cells[y][x] = MapFlags.SHIP;
        }

        return PlaceResult.OK;
    }

    private boolean touchesAnyShip(int x, int y) {
        // sprawdzenie 8 sasiadów czy jest SHIP lub SHIP_WRECKED
        for (int ny = y - 1; ny <= y + 1; ny++) {
            for (int nx = x - 1; nx <= x + 1; nx++) {
                if (nx == x && ny == y) continue;
                if (!inBounds(nx, ny)) continue;

                MapFlags f = cells[ny][nx];
                if (f == MapFlags.SHIP || f == MapFlags.SHIP_WRECKED) {
                    return true;
                }
            }
        }
        return false;
    }

    public ShotResult shoot(int x, int y) {
        requireInBounds(x, y);

        switch (cells[y][x]) {

	        case SHIP -> {
	            cells[y][x] = MapFlags.SHIP_WRECKED;
	            if(isShipSunkAt(x, y)) {
	            	markSunkShip(x, y);
	            	return ShotResult.SINK;
	            }
	            else
	             	return ShotResult.HIT;
	        }
            
            case SHIP_WRECKED -> {
                // już trafione
                return ShotResult.HIT;
            }

            case NOTHING -> {
                // pudło w wodę
                cells[y][x] = MapFlags.NO_SHIP;
                return ShotResult.MISS;
            }

            case NO_SHIP, TERRAIN -> {
                // już było pudło albo strzał w teren
                return ShotResult.MISS;
            }
        }

        throw new IllegalStateException("Unknown MapFlag");
    }


    private boolean isShipSunkAt(int x, int y) {
        // Ustalanie orientacji statku
        boolean hasHorizontalNeighbor = isShipPart(x - 1, y) || isShipPart(x + 1, y);
        boolean hasVerticalNeighbor = isShipPart(x, y - 1) || isShipPart(x, y + 1);

        if (hasHorizontalNeighbor && !hasVerticalNeighbor) {
            return scanLineSunk(x, y, 1, 0); // poziomo
        }
        if (hasVerticalNeighbor && !hasHorizontalNeighbor) {
            return scanLineSunk(x, y, 0, 1); // pionowo
        }

        // Single-cell statek
        if (!hasHorizontalNeighbor && !hasVerticalNeighbor) {
            return cells[y][x] == MapFlags.SHIP_WRECKED;
        }

        //nietype uklady - sprawdzenie obu kierunkow
        return scanLineSunk(x, y, 1, 0) && scanLineSunk(x, y, 0, 1);
    }

    private boolean scanLineSunk(int x, int y, int dx, int dy) {
        // pojscie do konca w lewo/górę
        int cx = x, cy = y;
        while (inBounds(cx - dx, cy - dy) && isShipPart(cx - dx, cy - dy)) {
            cx -= dx;
            cy -= dy;
        }

        // sprawdzenie od początku do końca czy istnieje żywy segment SHIP
        while (inBounds(cx, cy) && isShipPart(cx, cy)) {
            if (cells[cy][cx] == MapFlags.SHIP) {
                return false;
            }
            cx += dx;
            cy += dy;
        }

        return true;
    }
    
    public void markSunkShip(int x, int y) {
    	setFlag(MapFlags.SHIP_WRECKED, x, y);
        List<Coordinates> segments = getShipCoordinates(x, y);
        
        for (Coordinates c : segments) {

            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int sx = c.x + dx;
                    int sy = c.y + dy;

                    if (!inBounds(sx, sy)) continue;
                    if (cells[sy][sx] == MapFlags.NOTHING) {
                        cells[sy][sx] = MapFlags.NO_SHIP;
                    }
                }
            }
        }
    }

    public List<Coordinates> getShipCoordinates(int x, int y) {
        List<Coordinates> result = new ArrayList<>();
        boolean[][] visited = new boolean[size][size];
        dfs(x, y, result, visited);
        return result;
    }

    private void dfs(int x, int y, List<Coordinates> result, boolean[][] visited) {
        if (!inBounds(x, y)) return;
        if (visited[y][x]) return;
        if (cells[y][x] != MapFlags.SHIP_WRECKED) return;

        visited[y][x] = true;
        result.add(new Coordinates(x, y));

        dfs(x+1, y, result, visited);
        dfs(x-1, y, result, visited);
        dfs(x, y+1, result, visited);
        dfs(x, y-1, result, visited);
    }
    
    private boolean isShipPart(int x, int y) {
        if (!inBounds(x, y)) return false;
        return cells[y][x] == MapFlags.SHIP || cells[y][x] == MapFlags.SHIP_WRECKED;
    }
    public boolean isAllShipsSink() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (cells[y][x] == MapFlags.SHIP) return false;
            }
        }
        return true;
    }

    private void requireInBounds(int x, int y) {
        if (!inBounds(x, y)) {
            throw new IndexOutOfBoundsException("Out of board: (" + x + "," + y + ")");
        }
    }
}
