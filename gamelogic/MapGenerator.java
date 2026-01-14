package gamelogic;
import gamelogic.enums.*;

import java.util.Random;

public class MapGenerator {
    // Procent planszy, który ma zostać zajęty przez teren (wyspy)
    private static final double ISLAND_DENSITY = 0.08;

    // Maksymalna liczba osobnych wysp
    private static final int MAX_ISLANDS = 10;

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

    public static void generate(MapType mapType, Board board, int mapSize) {
        switch (mapType) {
            // Pełna woda – brak dodatkowego generowania
            case FULL_WATER -> {}
            // Generowanie wysp
            case ISLANDS -> generateIslands(board, mapSize);
        }
    }

    private static void generateIslands(Board board, int mapSize) {
        Random random = new Random();

        // Całkowita liczba pól na mapie
        int totalTiles = mapSize * mapSize;
        // Docelowa liczba pól terenu (wysp)
        int terrainTarget = (int) (totalTiles * ISLAND_DENSITY);

        // Tablica pomocnicza zapobiegająca wielokrotnemu ustawianiu terenu w tym samym miejscu
        boolean[][] placed = new boolean[mapSize][mapSize];

        // Liczba wysp – zależna od rozmiaru mapy, ale ograniczona MAX_ISLANDS
        int islands = Math.max(1, Math.min(MAX_ISLANDS, mapSize / 2));
        // Ile pól terenu pozostało do rozmieszczenia
        int remaining = terrainTarget;

        // Generowanie głównych wysp
        for (int i = 0; i < islands && remaining > 0; i++) {
            // Losowy punkt startowy wyspy
            int startX = random.nextInt(mapSize);
            int startY = random.nextInt(mapSize);

            // Maksymalny rozmiar aktualnej wyspy (tak, aby starczyło terenu na kolejne wyspy)
            int maxIslandSize = Math.max(1, remaining / (islands - i));

            // Losowy rozmiar wyspy
            int islandSize = 1 + random.nextInt(maxIslandSize);

            // Rozrastanie wyspy
            remaining -= growIsland(board, placed, mapSize, startX, startY, islandSize, random);
        }

        // Jeśli po wyspach zostały jeszcze pojedyncze pola terenu, rozmieszczamy je losowo
        while (remaining > 0) {
            int x = random.nextInt(mapSize);
            int y = random.nextInt(mapSize);

            if (!placed[y][x]) {
                placed[y][x] = true;
                board.setFlag( MapFlags.TERRAIN, x, y);
                remaining--;
            }
        }
    }

    private static int growIsland(
            Board board,
            boolean placed[][],
            int mapSize,
            int startX,
            int startY,
            int size,
            Random random
    ) {
        int x = startX;
        int y = startY;
        // Licznik nowych pól terenu
        int newlyPlaced = 0;

        // Jeśli pole nie było jeszcze terenem – ustawiamy je
        for (int i = 0; i < size; i++) {
            if (!placed[y][x]) {
                placed[y][x] = true;
                board.setFlag( MapFlags.TERRAIN, x, y);
                newlyPlaced++;
            }

            // Losowy ruch w jednym z czterech kierunków
            switch (random.nextInt(4)) {
                case 0 -> x = Math.max(0, x - 1);
                case 1 -> x = Math.min(mapSize - 1, x + 1);
                case 2 -> y = Math.max(0, y - 1);
                case 3 -> y = Math.min(mapSize - 1, y + 1);
            }
        }
        return newlyPlaced;
    }
}