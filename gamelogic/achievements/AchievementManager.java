package gamelogic.achievements;
import java.util.List;
import java.util.ArrayList;
import gamelogic.Event;
import gamelogic.GameListener;

public class AchievementManager implements GameListener {
    private final List<Achievement> achievements = new ArrayList<>();
    public void AchievementManager() {
        achievements.add(new LoseOneGame());
        achievements.add(new MissTenTimes());
        achievements.add(new WinOneGame());
        achievements.add(new ShootFiveShips());
    }
    public void addAchievement(Achievement achievement)
    {
        achievements.add(achievement);
    }
    @Override public void update(Event event) {
        for (Achievement achievement : achievements) {
            achievement.update(event);
        }
    }
}
