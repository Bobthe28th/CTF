package me.bobthe28th.bday.games.ctf;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.player.GamePlayer;
import me.bobthe28th.bday.games.GameState;
import me.bobthe28th.bday.scoreboard.ScoreboardObjective;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.HashMap;

public class CTF extends Game {

    private final HashMap<String,CTFTeam> teams = new HashMap<>();
    public CTF(Main plugin) {
        super(plugin);
        teams.put("Blue",new CTFTeam("Blue", ChatColor.BLUE, Material.BLUE_BANNER));
        teams.put("Red",new CTFTeam("Red", ChatColor.RED, Material.RED_BANNER));
        objective = new ScoreboardObjective("ctf","Capture the Flag");
        gameRules.setRule("pointstowin",3);
    }

    public HashMap<String,CTFTeam> getTeams() {
        return teams;
    }

    @Override
    public void onPlayerJoin(GamePlayer player) {

    }

    @Override
    public void onPlayerLeave(GamePlayer player) {

    }

    @Override
    public void start() {
        state = GameState.TEAMSELECT;
    }

    @Override
    public void disable() {
        objective.remove();
    }
}
