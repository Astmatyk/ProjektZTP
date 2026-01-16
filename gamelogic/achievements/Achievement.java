package gamelogic.achievements;
import gamelogic.Event;
public abstract class Achievement {
private boolean unlocked=false;
private String id;
public boolean isUnlocked() {
        return unlocked;
}
public void setId(String idToSet) { this.id = idToSet;}
public String getId() {return this.id;}
public void unlock() {
        if(!unlocked) unlocked=true;
}
public void update(Event event) {}
public abstract AchievementState saveState();
public abstract void loadState(AchievementState state);
}
