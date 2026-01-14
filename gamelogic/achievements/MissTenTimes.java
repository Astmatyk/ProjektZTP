package gamelogic.achievements;

import gamelogic.Event;
import gamelogic.enums.EventType;

public class MissTenTimes extends Achievement {
    private int count;
    @Override public void update(Event event) {
        if((event.type== EventType.SHOT_FIRED) && !event.result) {
            count++;
            if(count>=10)
            {
                unlock();
            }
        }
    }
}
