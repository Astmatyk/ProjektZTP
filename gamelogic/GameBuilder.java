package gamelogic;
import gamelogic.enums.*;

public interface GameBuilder {
    void buildBoard(MapType mapType);
    void buildPlayers();
    Game getResult(); // nowa gra (ID generuje Game)
    Game getResult(String gameId); // wczytana gra (ID z save)
}
