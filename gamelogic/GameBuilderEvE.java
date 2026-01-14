package gamelogic;
import gamelogic.enums.*;

public class GameBuilderEvE implements GameBuilder {

    private final int mapSize;
    private final BotDifficulty difficulty;

    private Board board1;
    private Board board2;

    private Player player1;
    private Player player2;

    public GameBuilderEvE(int mapSize, BotDifficulty difficulty) {
        this.mapSize = mapSize;
        this.difficulty = difficulty;
    }

    @Override
    public void buildBoard(MapType mapType) {
        board1 = new Board(mapSize);
        board2 = new Board(mapSize);

        MapGenerator.generate(mapType, board1, mapSize);
        MapGenerator.generate(mapType, board2, mapSize);
    }

    @Override
    public void buildPlayers() {
        if (board1 == null || board2 == null) {
            throw new IllegalStateException("Najpierw buildBoard()");
        }

        ShootingStrategy s1 = switch (difficulty) {
            case EASY -> new EasyMode();
            case NORMAL -> new NormalMode();
            case HARD -> new HardMode();
        };

        ShootingStrategy s2 = switch (difficulty) {
            case EASY -> new EasyMode();
            case NORMAL -> new NormalMode();
            case HARD -> new HardMode();
        };

        player1 = new PcPlayer(board1, board2, s1);
        player2 = new PcPlayer(board2, board1, s2);
    }

    @Override
    public Game getResult() {
        return getResult(null); // null => Game samo wygeneruje ID
    }

    @Override
    public Game getResult(String gameId) {
        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Najpierw buildPlayers()");
        }
        return new Game(player1, player2, gameId);
    }
}
