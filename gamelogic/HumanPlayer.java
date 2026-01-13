package gamelogic;

public class HumanPlayer extends Player {

    private Coordinates bufferedInput;

    private String name;
    public HumanPlayer(Board ownBoard, Board shootingBoard, String name) {
        super(ownBoard, shootingBoard);
        this.name=name;
    }

    // GUI wywołuje to przy kliknięciu
    public void setInput(Coordinates c) {
        bufferedInput = c;
    }

    @Override
    public Coordinates chooseCoordinates() {
        if (bufferedInput == null) throw new IllegalStateException("No input yet");
        Coordinates c = bufferedInput;
        bufferedInput = null;
        return c;
    }
}