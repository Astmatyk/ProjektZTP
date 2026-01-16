package gamelogic.achievements;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import gamelogic.Event;
import gamelogic.GameListener;

import javax.lang.model.type.NullType;

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
    public void saveToFile(File f) throws IOException {
        List<AchievementState> states = new ArrayList<>();

        for (Achievement a : achievements) {
            states.add(a.saveState());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {oos.writeObject(states);}
    }

    public void loadFromFile(File f) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(f))) {

            List<AchievementState> states = (List<AchievementState>) ois.readObject();

            for (AchievementState s : states) {
                findById(s.id).loadState(s);
            }
        }
    }
    private Achievement findById(String id) {
        for(Achievement achievement: this.achievements)
        {
            if(achievement.getId().equals( id)) {return achievement;}
        }
        return null;
    }
}
