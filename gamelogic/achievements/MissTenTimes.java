package gamelogic.achievements;

import gamelogic.Event;
import gamelogic.enums.EventType;

public class MissTenTimes extends Achievement {
    private int count;
    public MissTenTimes(){this.setId("Miss_Ten_Times");}
    @Override public void update(Event event) {
        if((event.type== EventType.SHOT_FIRED) && !event.result) {
            count++;
            if(count>=10)
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
