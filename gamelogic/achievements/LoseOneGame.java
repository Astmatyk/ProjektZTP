package gamelogic.achievements;
import gamelogic.GameEvent;
import gamelogic.enums.EventType;

public class LoseOneGame extends Achievement {
    @Override public void update(GameEvent event) {
        if((event.type==EventType.GAME_END) && !event.result) {
            unlock();
        }
        }
    }
