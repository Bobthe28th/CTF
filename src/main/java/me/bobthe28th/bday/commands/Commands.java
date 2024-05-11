package me.bobthe28th.bday.commands;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.managers.GameManager;
import me.bobthe28th.bday.music.Music;
import me.bobthe28th.bday.music.MusicManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Commands implements CommandExecutor {

    GameManager gameManager;
    MusicManager musicManager;

    String[] commandNames = new String[]{"test","music","join","leave","prepare","start"};

    public Commands(Main plugin) {
        this.gameManager = plugin.getGameManager();
        this.musicManager = plugin.getMusicManager();

        TabCompleter tabCompleter = new TabCompleter(gameManager, musicManager);
        for (String commandName : commandNames) {
            PluginCommand command = plugin.getCommand(commandName);
            if (command != null) {
                command.setExecutor(this);
                command.setTabCompleter(tabCompleter);
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "start":
                if (args.length == 0) {
                    if (gameManager.getCurrentGame() != null) {
                        player.sendMessage(ChatColor.GREEN + "Starting game");
                        gameManager.startGame();
                    } else {
                        player.sendMessage(ChatColor.RED + "No game is prepared");
                    }
                } else {
                    if (gameManager.getGameNames().contains(args[0])) {
                        player.sendMessage(ChatColor.GREEN + "Preparing and starting " + args[0]);
                        gameManager.startGame(gameManager.getGame(args[0]));
                    } else {
                        player.sendMessage(ChatColor.RED + "Game does not exist");
                    }
                }
            case "prepare":
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Provide a game");
                } else {
                    if (gameManager.getGameNames().contains(args[0])) {
                        player.sendMessage(ChatColor.GREEN + "Preparing " + args[0]);
                        gameManager.setGame(gameManager.getGame(args[0]));
                    } else {
                        player.sendMessage(ChatColor.RED + "Game does not exist");
                    }
                }
                return true;
            case "join":
                if (!gameManager.getGamePlayers().containsKey(player)) {
                    gameManager.addPlayer(player);
                    player.sendMessage(ChatColor.GREEN + "Joined!");
                } else {
                    player.sendMessage(ChatColor.RED + "You are already joined.");
                }
                return true;
            case "leave":
                if (gameManager.getGamePlayers().containsKey(player)) {
                    gameManager.removePlayer(player);
                    player.sendMessage(ChatColor.GREEN + "Left!");
                } else {
                    player.sendMessage(ChatColor.RED + "You never joined.");
                }
                return true;
            case "music": //TODO check and fix
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Provide an action");
                } else {
                    switch (args[0].toLowerCase()) {
                        case "clear":
                            musicManager.getQueue().clearQueue();
                            player.sendMessage(ChatColor.GREEN + "Cleared queue");
                            break;
                        case "start":
                            musicManager.start();
                            player.sendMessage(ChatColor.GREEN + "Started music");
                            break;
                        case "stop":
                            musicManager.stopCurrent();
                            player.sendMessage(ChatColor.GREEN + "Stopped playing current music");
                            break;
                        case "skip":
                            musicManager.playNext();
                            player.sendMessage(ChatColor.GREEN + "Skipped music");
                            break;
                        case "list":
                            List<String> q = musicManager.getQueue().getQueueName();
                            List<String> l = musicManager.getQueue().getLoopQueueName();
                            String c = musicManager.getCurrentlyPlayingName();
                            String currently = (c == null ? "Nothing is playing!\n\n" : "Currently playing: " + c + "\n\n");
                            if (q.isEmpty() && l.isEmpty()) {
                                player.sendMessage(currently + "The queue is empty!");
                                break;
                            } else {
                                StringBuilder qList = new StringBuilder("List queue:\n");
                                if (q.isEmpty()) {
                                    qList = new StringBuilder("List queue empty.\n");
                                } else {
                                    for (String qn : q) {
                                        qList.append(qn).append("\n");
                                    }
                                }
                                StringBuilder lList = new StringBuilder("Loop queue:\n");
                                if (l.isEmpty()) {
                                    lList = new StringBuilder("Loop queue empty.\n");
                                } else {
                                    for (String ln : l) {
                                        lList.append(ln).append("\n");
                                    }
                                }
                                player.sendMessage(currently + "Queue:\n" + qList + lList);
                            }
                            break;
                        case "play":
                            if (args[1] == null) {
                                player.sendMessage(ChatColor.RED + "Provide an play action");
                                break;
                            }
                            switch (args[1].toLowerCase()) {
                                case "now":
                                    if (args[2] != null) {
                                        Music m = musicManager.getMusicByName(args[2]);
                                        if (m != null) {
                                            musicManager.getQueue().addQueueStart(m);
                                            musicManager.playNext();
                                            player.sendMessage(ChatColor.RED + "Playing " + m.getName());
                                        } else {
                                            player.sendMessage(ChatColor.RED + "Invalid music");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Provide music name");
                                    }
                                    break;
                                case "queue":
                                    if (args[2] == null) {
                                        player.sendMessage(ChatColor.RED + "Provide a queue action");
                                        break;
                                    }
                                    Music m;
                                    if (args[3] != null) {
                                        m = musicManager.getMusicByName(args[3]);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Provide music name");
                                        break;
                                    }
                                    if (m == null) {
                                        player.sendMessage(ChatColor.RED + "Invalid music");
                                        break;
                                    }
                                    switch (args[2].toLowerCase()) {
                                        case "list" -> musicManager.getQueue().addQueue(m);
                                        case "loop" -> musicManager.getQueue().addLoopQueue(m);
                                    }
                                    player.sendMessage(ChatColor.GREEN + "Added " + m.getName() + " to the queue");
                                    break;
                            }
                    }
                }
                return true;
        }

        return false;
    }

}
