package gamelogic;
import gamelogic.enums.*;

import java.util.Random;

public class MapGenerator {

    private MapGenerator() {}

    public static Board fromLayout(int mapSize, boolean[][] layout) {
        Board board = new Board(mapSize);
        applyLayout(board, layout);
        return board;
    }

    private static void applyLayout(Board board, boolean[][] layout) {
        int n = layout.length;
        boolean[][] visited = new boolean[n][n];

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {

                if (layout[y][x] && !visited[y][x]) {

                    Board.Direction dir;
                    int len = 1;

                    // poziomy
                    if (x + 1 < n && layout[y][x + 1]) {
                        dir = Board.Direction.RIGHT;
                        int xx = x + 1;
                        while (xx < n && layout[y][xx]) {
                            visited[y][xx] = true;
                            len++;
                            xx++;
                        }
                    }
                    // pionowy
                    else if (y + 1 < n && layout[y + 1][x]) {
                        dir = Board.Direction.DOWN;
                        int yy = y + 1;
                        while (yy < n && layout[yy][x]) {
                            visited[yy][x] = true;
                            len++;
                            yy++;
                        }
                    }
                    // jednomasztowiec
                    else {
                        dir = Board.Direction.RIGHT;
                    }

                    visited[y][x] = true;

                    Board.PlaceResult res =
                            board.placeShip(x, y, len, dir);

                    if (res != Board.PlaceResult.OK) {
                        throw new IllegalStateException(
                                "Niepoprawna konfiguracja planszy"
                        );
                    }
                }
            }
        }
    }
}