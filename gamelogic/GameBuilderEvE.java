package gamelogic;
import gamelogic.enums.*;

public class GameBuilderEvE implements GameBuilder {
    private final int mapSize;
    private final String bot1Name;
    private final String bot2Name;
    private final BotDifficulty difficulty;

    private final boolean[][] bot1Layout;
    private final boolean[][] bot2Layout;

    private Board board1;
    private Board board2;

    private Player player1;
    private Player player2;

    public GameBuilderEvE(int mapSize, String player1Name, String player2Name, boolean[][] p1Layout, boolean[][] p2Layout, BotDifficulty difficulty) {
        this.mapSize = mapSize;
        this.bot1Name = player1Name;
        this.bot2Name = player2Name;
        this.difficulty = difficulty;
        this.bot1Layout = p1Layout;
        this.bot2Layout = p2Layout;
    }

    @Override
    public void buildBoard() {
        board1 = MapGenerator.fromLayout(mapSize, bot1Layout);
        board2 = MapGenerator.fromLayout(mapSize, bot2Layout);
    }

    private void placeShipsRandomly(Board board) {
        int[] shipLengths = {5, 4, 3, 3, 2}; 
        for (int length : shipLengths) {
            boolean placed = false;
            while (!placed) {
                int x = (int)(Math.random() * board.getSize());
                int y = (int)(Math.random() * board.getSize());
                Board.Direction dir = Board.Direction.values()[(int)(Math.random() * 4)];
                Board.PlaceResult result = board.placeShip(x, y, length, dir);
                if (result == Board.PlaceResult.OK) {
                    placed = true;
                }
            }
        }
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
        
        Board sBoard1=new Board(mapSize);
        Board sBoard2=new Board(mapSize);

        player1 = new PcPlayer(board1, sBoard1, s1);
        player2 = new PcPlayer(board2, sBoard2, s2);
    }

    @Override
    public Game getResult() {
        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Najpierw buildPlayers()");
        }
        return new Game(player1, player2);
    }
}
