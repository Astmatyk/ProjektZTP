package gamelogic;
import gamelogic.enums.*;

public class GameBuilderPvE implements GameBuilder {

    private final int mapSize;
    private final String humanName;
    private final BotDifficulty difficulty;

    private final boolean[][] playerLayout;

    private Board board1;
    private Board board2;

    private Player player1;
    private Player player2;

    //Tymczasowy konstrukto
    public GameBuilderPvE(int mapSize, String humanName, BotDifficulty difficulty, boolean[][] playerLayout) {
        this.mapSize = mapSize;
        this.humanName = humanName;
        this.difficulty = difficulty;
        this.playerLayout = playerLayout;
    }

    @Override
    public void buildBoard() {
        board1 = MapGenerator.fromLayout(mapSize, playerLayout);
        board2 = new Board(mapSize);
        
        placeShipsRandomly(board2);
    }
    
    private void placeShipsRandomly(Board board) {
        int[] shipLengths = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; 
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



        ShootingStrategy strategy = switch (difficulty) {
            case EASY -> new EasyMode();
            case NORMAL -> new NormalMode();
            case HARD -> new HardMode();
        };

        Board sBoard1=new Board(mapSize);
        Board sBoard2=new Board(mapSize);

        player1 = new HumanPlayer(board1, sBoard1, humanName);
        player2 = new PcPlayer(board2, sBoard2, strategy);
    }
    
    @Override
    public Game getResult() {
        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Najpierw buildPlayers()");
        }
        return new Game(player1, player2);
    }
}

