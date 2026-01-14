package gamelogic;
import gamelogic.enums.EventType;
public class Event {
    public final EventType type;
    public final boolean result;
    public final Player player;

    public Event(EventType type, boolean result, Player player) {
        this.type = type;
        this.result = result;
        this.player = player;
    }
}