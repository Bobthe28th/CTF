package me.bobthe28th.ctf;

import me.bobthe28th.ctf.games.GameManager;
import me.bobthe28th.ctf.music.MusicManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private GameManager gameManager;
    private MusicManager musicManager;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "This is a test!");
        gameManager = new GameManager(this);
        musicManager = new MusicManager(this);
        new GameManager(this);
    }

    @Override
    public void onDisable() {
        musicManager.disable();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }
}