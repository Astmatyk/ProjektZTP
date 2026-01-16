package gui.gameconfig;

public class EvEBoardConfigurator extends BoardConfiguratorAbstract {

    public EvEBoardConfigurator(int size){
        super(size);
        System.out.println("EvE - TEST");
        this.player1Board = new boolean[size][size];
        this.player2Board = new boolean[size][size];
        player1Board = placeRandomFleet(size);
        player2Board = placeRandomFleet(size);
    }
}
