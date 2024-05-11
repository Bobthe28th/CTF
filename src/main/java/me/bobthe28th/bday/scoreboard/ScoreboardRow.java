package me.bobthe28th.bday.scoreboard;

import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

public class ScoreboardRow {

    private final int value;
    private String data;
    private final HashMap<ScoreboardController, String> localData = new HashMap<>();
    private final ScoreboardObjective objective;
    private final boolean global;

    public ScoreboardRow(int value, String data, ScoreboardObjective objective, boolean global) {
        this.value = value;
        this.data = data;
        this.objective = objective;
        this.global = global;
    }

    public void addPlayer(ScoreboardController controller) {
        objective.getObjective(controller).getScore(data).setScore(value);
        if (global) localData.put(controller, data);
    }

    public void update(String data, ScoreboardController controller) {
        String oldData = localData.get(controller);
        localData.replace(controller,data);
        Objective o = objective.getObjective(controller);
        controller.getScoreboard().resetScores(oldData);
        o.getScore(data).setScore(value);
    }

    public void update(String data) {
        String oldData = this.data;
        this.data = data;
        for (ScoreboardController controller : objective.getObjectives().keySet()) {
            Objective o = objective.getObjective(controller);
            if (global) controller.getScoreboard().resetScores(oldData);
            else {
                controller.getScoreboard().resetScores(localData.get(controller));
                localData.replace(controller,data);
            }
            o.getScore(data).setScore(value);
        }
    }

}
