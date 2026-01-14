package gamelogic;
import gamelogic.enums.*;

public interface GameBuilder {
    void buildBoard();
    void buildPlayers();
    Game getResult(); // nowa gra (ID generuje Game)
    Game getResult(String gameId); // wczytana gra (ID z save)
}
