package gamelogic.achievements;
import gamelogic.GameEvent;
public abstract class Achievement {
private boolean unlocked=false;

public boolean isUnlocked() {
        return unlocked;
}
public void unlock() {
        if(!unlocked) unlocked=true;
}
public void update(GameEvent event) {}

}
