package me.bobthe28th.bday.games;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.ctf.CTFTeam;
import me.bobthe28th.bday.games.gamerules.GameRules;
import me.bobthe28th.bday.games.managers.GameManager;
import me.bobthe28th.bday.games.player.GamePlayer;
import me.bobthe28th.bday.scoreboard.ScoreboardObjective;
import org.bukkit.World;

import java.util.HashMap;

public abstract class Game {

    protected final World world;
    protected GameState state = GameState.LOBBY;
    protected final Main plugin;
    protected final GameManager manager;
    protected ScoreboardObjective objective;
    protected GameRules gameRules = new GameRules();
    protected boolean enabled = false;

    protected final HashMap<String, GameTeam> teams = new HashMap<>();

    protected GameMap map;

    public Game(Main plugin, World world) {
        enabled = true;
        this.plugin = plugin;
        this.world = world;
        this.manager = plugin.getGameManager();
        gameRules.setRule("time",0);
    }

    public abstract void onPlayerJoin(GamePlayer player);
    public abstract void onPlayerLeave(GamePlayer player);

    public abstract void start();

    public abstract void disable();

    public void end() {
        disable();
        state = GameState.END;
    }

    public World getWorld() {
        return world;
    }

    public HashMap<String, GameTeam> getTeams() {
        return teams;
    }

    public GameManager getManager() {
        return manager;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Main getPlugin() {
        return plugin;
    }
}
