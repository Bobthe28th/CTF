package me.bobthe28th.bday.games.ctf;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.managers.ClassSelectionManager;
import me.bobthe28th.bday.games.managers.TeamSelectionManager;
import me.bobthe28th.bday.games.player.GamePlayer;
import me.bobthe28th.bday.games.GameState;
import me.bobthe28th.bday.scoreboard.ScoreboardObjective;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;

public class CTF extends Game {


    public CTF(Main plugin, World world) {
        super(plugin, world);
        teams.put("Blue",new CTFTeam("Blue", ChatColor.BLUE, Material.BLUE_CONCRETE, Material.BLUE_BANNER));
        teams.put("Red",new CTFTeam("Red", ChatColor.RED, Material.RED_CONCRETE, Material.RED_BANNER));
        objective = new ScoreboardObjective("ctf","Capture the Flag");
        gameRules.setRule("pointstowin",3);
    }

    @Override
    public void onPlayerJoin(GamePlayer player) {

    }

    @Override
    public void onPlayerLeave(GamePlayer player) {

    }

    @Override
    public void start() {
//        state = GameState.TEAMSELECT;
        new TeamSelectionManager(this).start();
    }

    @Override
    public void disable() {
        enabled = false;
        objective.remove();
    }
}
