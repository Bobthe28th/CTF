package me.bobthe28th.bday.games.managers;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.Game;
import me.bobthe28th.bday.games.ctf.CTF;
import me.bobthe28th.bday.games.player.GamePlayer;
import me.bobthe28th.bday.games.GameState;
import me.bobthe28th.bday.games.rule.DamageRule;
import me.bobthe28th.bday.games.rule.MoveRule;
import me.bobthe28th.bday.util.TextUtil;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager implements Listener {

    private final Main plugin;
    private DamageRule damageRule = DamageRule.ALL;
    private boolean breakBlocks = false;
    private MoveRule moveRule = MoveRule.ALL;
    private final HashMap<Player, GamePlayer> gamePlayers = new HashMap<>();
    private final HashMap<String,Class<? extends Game>> gameList = new HashMap<>();

    private Game currentGame = null;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        gameList.put("ctf", CTF.class);
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            gamePlayers.put(player,new GamePlayer(plugin,player));
        }
    }

    public void disable() {
        HandlerList.unregisterAll(this);
    }

    public Class<? extends Game> getGame(String name) {
        return gameList.get(name);
    }

    public HashMap<String, Class<? extends Game>> getGames() {
        return gameList;
    }

    public ArrayList<String> getGameNames() {
        return new ArrayList<>(gameList.keySet());
    }

    public void setGame(Class<? extends Game> game) {
        if (currentGame != null) {
            currentGame.disable();
        }
        if (game != null) {
            try {
                Constructor<? extends Game> constructor = game.getConstructor(Main.class, World.class);
                currentGame = constructor.newInstance(plugin, Bukkit.getWorlds().getFirst());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startGame() {
        if (currentGame == null) return;
        if (currentGame.getState() != GameState.LOBBY) return;
        currentGame.start();
    }

    public void startGame(Class<? extends Game> game) {
        setGame(game);
        startGame();
    }

    public Game getCurrentGame() {
        return currentGame;
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

    //TODO swap hands, armor stand, interact with item frame, drop item

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!gamePlayers.containsKey(player)) return;
            player.setFoodLevel(20);
            player.setSaturation(0F);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (!gamePlayers.containsKey(event.getPlayer())) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!gamePlayers.containsKey(event.getPlayer())) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (event.getRightClicked() instanceof ItemFrame || event.getRightClicked() instanceof GlowItemFrame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!gamePlayers.containsKey(event.getPlayer())) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (damageRule == DamageRule.NONE && event.getCause() != EntityDamageEvent.DamageCause.VOID && event.getEntity() instanceof Player) {
            event.setCancelled(true);
            return;
        } else if (damageRule == DamageRule.NONPLAYER) {
            if (event instanceof EntityDamageByEntityEvent byEntityEvent && byEntityEvent.getDamager() instanceof Player) {
                event.setCancelled(true);
                return;
            }
        }
        if (!(event.getEntity() instanceof LivingEntity damaged)) return;
        if (damaged instanceof Player pDamaged) {
            if (gamePlayers.containsKey(pDamaged)) {
                gamePlayers.get(pDamaged).takeDamage(event.getFinalDamage());
            }
        }
        for (GamePlayer player : gamePlayers.values()) {
            if (player.getEnemyHealthBar().getEnemy() == damaged) {
                player.getEnemyHealthBar().updateEnemyHealth(damaged, event.getFinalDamage());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof LivingEntity damaged)) return;
        if (!(event.getDamager() instanceof Player damager)) return;
        if (gamePlayers.containsKey(damager)) {
            gamePlayers.get(damager).damage(damaged, event.getFinalDamage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED && event.getEntity() instanceof Player player && gamePlayers.containsKey(player)) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof LivingEntity entity) {
            for (GamePlayer player : gamePlayers.values()) {
                if (player.getEnemyHealthBar().getEnemy() == entity) {
                    player.getEnemyHealthBar().updateEnemyHealth(entity, -event.getAmount());
                }
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
            if (event.getTo() == null) return;
            if (event.getTo().toVector().equals(event.getFrom().toVector())) return;
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
        addPlayer(event.getPlayer());
    }

    public void addPlayer(Player player) {
        gamePlayers.put(player,new GamePlayer(plugin,player));
        if (currentGame != null && currentGame.getState() != GameState.END) { //TODO remove and add end join state?
            currentGame.onPlayerJoin(gamePlayers.get(player));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.YELLOW + event.getPlayer().getDisplayName() + " left");
        if (gamePlayers.get(event.getPlayer()) != null) {
            removePlayer(event.getPlayer());
        }
    }

    public void removePlayer(Player player) {
        if (currentGame != null && currentGame.getState() != GameState.END) {
            currentGame.onPlayerLeave(gamePlayers.get(player));
        }
        gamePlayers.remove(player);
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
