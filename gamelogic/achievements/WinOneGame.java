package gamelogic.achievements;

import gamelogic.Event;
import gamelogic.enums.EventType;

public class WinOneGame extends Achievement {
    public WinOneGame(){this.setId("Win_One_Game");}
    @Override public void update(Event event) {
        if((event.type== EventType.GAME_END) && !event.result) {
            unlock();
        }
    }
    @Override
    public AchievementState saveState() {
        AchievementState state = new AchievementState();
        state.id = this.getId();
        state.unlocked = this.isUnlocked();
        return state;
    }

    @Override
    public void loadState(AchievementState state) {
        if(state.unlocked)
        {
            this.unlock();
        }
    }
}
