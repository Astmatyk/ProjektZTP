package gamelogic;
import gamelogic.enums.*;

import java.util.Random;

public class MapGenerator {
    private static final double ISLAND_DENSITY = 0.08;
    private static final int MAX_ISLANDS = 10;

    private MapGenerator() {}

    public static void generate(MapType mapType, Board board, int mapSize) {
        switch (mapType) {
            case FULL_WATER -> {}
            case ISLANDS -> generateIslands(board, mapSize);
        }
    }

    private static void generateIslands(Board board, int mapSize) {
        Random random = new Random();

        int totalTiles = mapSize * mapSize;
        int terrainTarget = (int) (totalTiles * ISLAND_DENSITY);

        boolean[][] placed = new boolean[mapSize][mapSize];

        int islands = Math.max(1, Math.min(MAX_ISLANDS, mapSize / 2));
        int remaining = terrainTarget;

        for (int i = 0; i < islands && remaining > 0; i++) {
            int startX = random.nextInt(mapSize);
            int startY = random.nextInt(mapSize);

            int maxIslandSize = Math.max(1, remaining / (islands - i));
            int islandSize = 1 + random.nextInt(maxIslandSize);

            remaining -= growIsland(board, placed, mapSize, startX, startY, islandSize, random);
        }

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
        int newlyPlaced = 0;

        for (int i = 0; i < size; i++) {
            if (!placed[y][x]) {
                placed[y][x] = true;
                board.setFlag( MapFlags.TERRAIN, x, y);
                newlyPlaced++;
            }

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