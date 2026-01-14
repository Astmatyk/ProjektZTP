package gamelogic.achievements;

import gamelogic.Event;
import gamelogic.enums.EventType;

public class WinOneGame extends Achievement {
    @Override public void update(Event event) {
        if((event.type== EventType.GAME_END) && !event.result) {
            unlock();
        }
    }
}
