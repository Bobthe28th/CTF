package me.bobthe28th.bday.games.managers;

import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.GameState;
import me.bobthe28th.bday.games.GameTeam;

import java.util.HashMap;

public class TeamSelectionManager {

    Game game;

    public TeamSelectionManager(Game game) {
        this.game = game;
    }

    public void start() {
        game.setState(GameState.TEAMSELECT);
        // set up team select zone
        for (GameTeam team : game.getTeams().values()) {
            team.getTeamSelectBlock();
        }
        // teleport everyone

    }

}
