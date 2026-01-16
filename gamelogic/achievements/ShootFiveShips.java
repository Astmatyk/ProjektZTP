package gamelogic.achievements;
import gamelogic.Event;
import gamelogic.enums.EventType;

public class ShootFiveShips extends Achievement {
    private int count;
    public ShootFiveShips(){this.setId("Shoot_Five_Ships");}
    @Override public void update(Event event) {
        if((event.type==EventType.SHOT_FIRED) && event.result) {
            count++;
            if(count>=5)
            {
                unlock();
            }
        }
    }
    @Override
    public AchievementState saveState() {
        AchievementState state = new AchievementState();
        state.id=this.getId();
        state.unlocked=isUnlocked();
        state.progress=this.count;
        return state;
    }

    @Override
    public void loadState(AchievementState state) {
        if(state.unlocked)
        {
            this.unlock();
        }
        this.count=state.progress;
    }
}
