package me.bobthe28th.ctf.games;

import me.bobthe28th.ctf.Main;
import me.bobthe28th.ctf.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class GameManager implements Listener {

    private final Main plugin;
    private DamageRule damageRule = DamageRule.NONE;
    private boolean breakBlocks = false;
    private final HashMap<Player,GamePlayer> gamePlayers = new HashMap<>();

    private Game currentGame = null;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    public HashMap<Player, GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setDamageRule(DamageRule damageRule) {
        this.damageRule = damageRule;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public DamageRule damageRule() {
        return damageRule;
    }

    public boolean breakBlocks() {
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
        for(String criteria: advancement.getCriteria()) {
            player.getAdvancementProgress(advancement).revokeCriteria(criteria);
        }
    }
}
