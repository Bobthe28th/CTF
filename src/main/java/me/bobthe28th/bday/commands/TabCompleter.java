package me.bobthe28th.bday.commands;

import me.bobthe28th.bday.games.managers.GameManager;
import me.bobthe28th.bday.music.MusicManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    GameManager gameManager;
    MusicManager musicManager;

    public TabCompleter(GameManager gameManager, MusicManager musicManager) {
        this.gameManager = gameManager;
        this.musicManager = musicManager;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        switch (command.getName().toLowerCase()) {
            case "start":
            case "prepare":
                if (args.length == 1) {
                    return gameManager.getGameNames();
                }
                break;
            case "music":
                switch (args.length) {
                    case 1:
                        return Arrays.asList("start","play","stop","skip","list","clear");
                    case 2:
                        if (args[0].equals("play")) {
                            return Arrays.asList("now","queue");
                        }
                        break;
                    case 3:
                        if (args[0].equals("play")) {
                            if (args[1].equals("now")) {
                                return musicManager.getMusicNameList();
                            } else if (args[1].equals("queue")) {
                                return Arrays.asList("list","loop");
                            }
                        }
                        break;
                    case 4:
                        if (args[0].equals("play") && args[1].equals("queue")) {
                            return musicManager.getMusicNameList();
                        }
                        break;
                }
                break;
        }
        return new ArrayList<>();
    }

}
