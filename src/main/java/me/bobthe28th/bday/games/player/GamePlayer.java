package me.bobthe28th.bday.games.player;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.classes.items.GameItem;
import me.bobthe28th.bday.scoreboard.ScoreboardController;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class GamePlayer {

    protected final Player player;
    protected final Main plugin;
    private final ScoreboardController scoreboardController;
    private final EnemyHealthBar enemyHealthBar;
    private final Regen regen;
    boolean isAlive;
    protected ArrayList<? extends GameItem> items = new ArrayList<>();

    public GamePlayer(Main plugin, Player player) {
        //plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.player = player;
        this.plugin = plugin;
        this.player.setLevel(0);
        this.player.setExp(0.0F);
        this.player.setFoodLevel(20);
        this.player.setSaturation(0F);
        this.player.setGlowing(false);
        this.player.setInvisible(false);
        this.player.setPlayerListHeaderFooter("Deez", "Nuts");
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        scoreboardController = new ScoreboardController(this);
        enemyHealthBar = new EnemyHealthBar(this, plugin);
        regen = new Regen(plugin, this);
    }

    public Player getPlayer() {
        return player;
    }

    public ScoreboardController getScoreboardController() {
        return scoreboardController;
    }

    public void takeDamage(double damage) {
        regen.startHealCooldown();
    }

    public void damage(LivingEntity entity, double damage) {
        enemyHealthBar.startEnemyHealthCooldown();
        enemyHealthBar.updateEnemyHealth(entity, damage);
    }

    public EnemyHealthBar getEnemyHealthBar() {
        return enemyHealthBar;
    }

    public void giveItem(GameItem item) {
        int slot = item.getSlot();
        if (items.get(slot) != null) {
            if (items.get(slot).getDefaultSlot() != slot) {
                items.get(slot).setSlot(items.get(slot).getDefaultSlot());
            } else {
                items.get(slot).remove();
            }
        }
        player.getInventory().setItem(slot, item.getItem());
    }

    public void remove() {
        removeNotMap();
        plugin.getGameManager().getGamePlayers().remove(player);
    }

    public void removeNotMap() {
        //HandlerList.unregisterAll(this);
    }
}
