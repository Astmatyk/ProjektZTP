package gamelogic;
import gamelogic.enums.*;

public class GameBuilderPvP implements GameBuilder {
    private final int mapSize;
    private final String player1Name;
    private final String player2Name;

    private final boolean[][] p1Layout;
    private final boolean[][] p2Layout;

    private Board board1;
    private Board board2;

    private Player player1;
    private Player player2;

    public GameBuilderPvP(int mapSize, String player1Name, String player2Name, boolean[][] p1Layout, boolean[][] p2Layout) {
        this.mapSize = mapSize;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.p1Layout = p1Layout;
        this.p2Layout = p2Layout;
    }

    @Override
    public void buildBoard() {
        board1 = MapGenerator.fromLayout(mapSize, p1Layout);
        board2 = MapGenerator.fromLayout(mapSize, p2Layout);
    }

    @Override
    public void buildPlayers() {
        if (board1 == null || board2 == null) {
            throw new IllegalStateException("najpierw plansze");
        }
        
        Board sBoard1=new Board(mapSize);
        Board sBoard2=new Board(mapSize);
        
        this.player1 = new HumanPlayer(board1, sBoard1, player1Name);
        this.player2 = new HumanPlayer(board2, sBoard2, player2Name);
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
