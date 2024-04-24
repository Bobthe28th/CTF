package me.bobthe28th.bday;

import me.bobthe28th.bday.commands.Commands;
import me.bobthe28th.bday.games.managers.GameManager;
import me.bobthe28th.bday.music.MusicManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private GameManager gameManager;
    private MusicManager musicManager;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "This is a test 4!");
        gameManager = new GameManager(this);
        musicManager = new MusicManager(this);
        new Commands(this);
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