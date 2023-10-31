package me.bobthe28th.bday.games.managers;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.GamePlayer;
import me.bobthe28th.bday.games.GameState;
import me.bobthe28th.bday.games.rule.DamageRule;
import me.bobthe28th.bday.games.rule.MoveRule;
import me.bobthe28th.bday.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class GameManager implements Listener {

    private final Main plugin;
    private DamageRule damageRule = DamageRule.NONE;
    private boolean breakBlocks = false;
    private MoveRule moveRule = MoveRule.ALL;
    private final HashMap<Player, GamePlayer> gamePlayers = new HashMap<>();

    private Game currentGame = null;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        //plugin.getServer().getPluginManager().registerEvents(this,plugin);
        //if (Bukkit.getScoreboardManager() != null) {
        //    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        //    for (Team team : scoreboard.getTeams()) {
        //        if (team.getName().startsWith("gameteam_")) {
        //            team.unregister();
        //        }
        //    }
        //}
    }

    public void setGame(Class<? extends Game> game) {
        if (currentGame != null) {
            currentGame.disable();
        }
        if (game != null) {
            try {
                Constructor<? extends Game> constructor = game.getConstructor(Main.class);
                currentGame = constructor.newInstance(plugin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<Player, GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setMoveRule(MoveRule moveRule) {
        this.moveRule = moveRule;
    }

    public void setDamageRule(DamageRule damageRule) {
        this.damageRule = damageRule;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public MoveRule getMoveRule() {
        return moveRule;
    }

    public DamageRule getDamageRule() {
        return damageRule;
    }

    public boolean getBreakBlocks() {
        return breakBlocks;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (damageRule == DamageRule.NONE && event.getCause() != EntityDamageEvent.DamageCause.VOID && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        } else if (damageRule == DamageRule.NONPLAYER) {
            if (event instanceof EntityDamageByEntityEvent byEntityEvent && byEntityEvent.getDamager() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (breakBlocks) return;
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (gamePlayers.containsKey(event.getPlayer())) return;
        if (moveRule == MoveRule.ALL) return;
        if (moveRule == MoveRule.NONE) {
            event.setCancelled(true);
        } else {
            if (event.getTo() != null && event.getTo().toVector().equals(event.getFrom().toVector())) return;
            if (moveRule == MoveRule.LOOK) {
                Location l = event.getFrom().clone();
                l.setYaw(event.getTo().getYaw());
                l.setPitch(event.getTo().getPitch());
                event.setTo(l);
            } else {
                if (moveRule == MoveRule.VERTICAL) {
                    Vector diff = event.getTo().toVector().subtract(event.getFrom().toVector());
                    if (diff.getX() != 0 || diff.getY() != 0) {
                        Location l = event.getFrom().clone();
                        l.setYaw(event.getTo().getYaw());
                        l.setPitch(event.getTo().getPitch());
                        l.setY(event.getTo().getY());
                        event.setTo(l);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.YELLOW + event.getPlayer().getDisplayName() + " joined");
        gamePlayers.put(event.getPlayer(),new GamePlayer(plugin,event.getPlayer()));
        if (currentGame != null && currentGame.getState() != GameState.END) { //TODO remove and add end join state?
            currentGame.onPlayerJoin(gamePlayers.get(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.YELLOW + event.getPlayer().getDisplayName() + " left");
        if (gamePlayers.get(event.getPlayer()) != null) {
            if (currentGame != null && currentGame.getState() != GameState.END) {
                currentGame.onPlayerLeave(gamePlayers.get(event.getPlayer()));
            }
            gamePlayers.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().getName().equals("Bobthe29th")) {
            event.setFormat(TextUtil.rainbow(event.getPlayer().getDisplayName()) + ChatColor.RESET + ": " + event.getMessage().replace("%","%%"));
        } else {
            event.setFormat(event.getPlayer().getDisplayName() + ": " + event.getMessage().replace("%","%%"));
        }
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();
        if (advancement.getDisplay() != null && advancement.getDisplay().getDescription().startsWith("\ue240")) return;
        for(String criteria : advancement.getCriteria()) {
            player.getAdvancementProgress(advancement).revokeCriteria(criteria);
        }
    }
}
