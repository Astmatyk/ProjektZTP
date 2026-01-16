package gamelogic;
import gamelogic.enums.*;

public interface GameBuilder {
    void buildBoard();
    void buildPlayers();
    Game getResult();
}
