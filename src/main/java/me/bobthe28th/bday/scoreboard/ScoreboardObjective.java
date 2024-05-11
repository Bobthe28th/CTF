package me.bobthe28th.bday.scoreboard;

import me.bobthe28th.bday.games.player.GamePlayer;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

public class ScoreboardObjective {

    HashMap<ScoreboardController, Objective> objectives = new HashMap<>();
    HashMap<Integer, ScoreboardRow> rows = new HashMap<>();
    String title;
    String displayTitle;

    public ScoreboardObjective(String title) {
        this(title, title);
    }
    public ScoreboardObjective(String title, String displayTitle) {
        this.title = title;
        this.displayTitle = displayTitle;
    }

    public void addRow(int value, String data, boolean global) {
        ScoreboardRow row = new ScoreboardRow(value, data, this, global);
        rows.put(value,row);
        for (ScoreboardController controller : objectives.keySet()) {
            row.addPlayer(controller);
        }
    }

    public void addPlayer(ScoreboardController controller) {
        Objective o = controller.getScoreboard().registerNewObjective(title, Criteria.DUMMY, displayTitle);
        objectives.put(controller, o);
        for (ScoreboardRow row : rows.values()) {
            row.addPlayer(controller);
        }
    }

    public void updateRow(int value, String data) {
        if (rows.containsKey(value)) {
            rows.get(value).update(data);
        }
    }

    public void updateRow(int value, String data, ScoreboardController controller) {
        if (rows.containsKey(value)) {
            rows.get(value).update(data, controller);
        }
    }

    public void updateRow(int value, String data, GamePlayer player) {
        if (rows.containsKey(value)) {
            rows.get(value).update(data, player.getScoreboardController());
        }
    }

    public void remove() {
        for (ScoreboardController controller : objectives.keySet()) {
            objectives.get(controller).unregister();
        }
        objectives.clear();
    }

    public HashMap<ScoreboardController, Objective> getObjectives() {
        return objectives;
    }

    public Objective getObjective(ScoreboardController controller) {
        return objectives.get(controller);
    }

    public void removePlayer(ScoreboardController p) {
        objectives.get(p).unregister();
        objectives.remove(p);
    }
}
