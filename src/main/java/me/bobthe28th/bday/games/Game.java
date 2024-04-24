package me.bobthe28th.bday.games;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.gamerules.GameRules;
import me.bobthe28th.bday.games.managers.GameManager;
import me.bobthe28th.bday.games.player.GamePlayer;
import me.bobthe28th.bday.scoreboard.ScoreboardObjective;

public abstract class Game {

    protected GameState state = GameState.LOBBY;
    protected final Main plugin;
    protected final GameManager manager;
    protected ScoreboardObjective objective;
    protected GameRules gameRules;

    protected GameMap map;

    public Game(Main plugin) {
        this.plugin = plugin;
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

    public GameManager getManager() {
        return manager;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }
}
