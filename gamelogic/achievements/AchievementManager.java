package gamelogic.achievements;
import gamelogic.GameEvent;
import gamelogic.GameListener;
import java.util.ArrayList;
import java.util.List;

public class AchievementManager implements GameListener {
    private final List<Achievement> achievements = new ArrayList<>();
    public void AchievementManager() {}
    public void addAchievement(Achievement achievement)
    {
        achievements.add(achievement);
    }
    @Override public void update(GameEvent event) {
        for (Achievement achievement : achievements) {
            achievement.update(event);
        }
    }
}
