package gamelogic.achievements;
import gamelogic.Event;
import gamelogic.enums.EventType;

public class ShootFiveShips extends Achievement {
    private int count;
    @Override public void update(Event event) {
        if(event.type== EventType.SHIP_SHOT) {
            count++;
            if(count>=5)
            {
                unlock();
            }
        }
    }
}
