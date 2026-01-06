package gamelogic;

public class HumanPlayer extends Player {
    private String name;

    public HumanPlayer(Board ownBoard, Board shootingBoard, String name) {
        super(ownBoard, shootingBoard);
        this.name = name;
    }
}
