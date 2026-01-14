package gamelogic.achievements;
import gamelogic.Event;
public abstract class Achievement {
private boolean unlocked=false;

public boolean isUnlocked() {
        return unlocked;
}
public void unlock() {
        if(!unlocked) unlocked=true;
}
public void update(Event event) {}

}
