package gamelogic;
import gamelogic.enums.*;

public interface GameBuilder {
    void buildBoard(MapType mapType);
    void buildPlayers();
    void buildProxies();
    Game getResult();
}
