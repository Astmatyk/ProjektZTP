package gamelogic.achievements;
import java.util.List;
import java.util.ArrayList;
import gamelogic.Event;
import gamelogic.GameListener;

public class AchievementManager implements GameListener {
    private final List<Achievement> achievements = new ArrayList<>();
    public void AchievementManager()
    {
        this.achievements.add(new LoseOneGame());
        this.achievements.add(new WinOneGame());
        this.achievements.add(new ShootFiveShips());
        this.achievements.add(new MissTenTimes());
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
