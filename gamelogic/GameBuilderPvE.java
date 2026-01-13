package gamelogic;
import gamelogic.enums.*;

public class GameBuilderPvE implements GameBuilder {

    private final int mapSize;
    private final String humanName;
    private final BotDifficulty difficulty;

    private Board board1;
    private Board board2;

    private Player player1;
    private Player player2;

    public GameBuilderPvE(int mapSize, String humanName, BotDifficulty difficulty) {
        this.mapSize = mapSize;
        this.humanName = humanName;
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

        player1 = new HumanPlayer(board1, board2, humanName);

        ShootingStrategy strategy = switch (difficulty) {
            case EASY -> new EasyMode();
            case NORMAL -> new NormalMode();
            case HARD -> new HardMode();
        };

        player2 = new PcPlayer(board2, board1, strategy);
    }

    @Override
    public void buildProxies() {

    }

    @Override
    public Game getResult() {
        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Najpierw buildPlayers()");
        }
        return new Game(player1, player2);
    }
}

