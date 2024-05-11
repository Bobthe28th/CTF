package me.bobthe28th.bday.games.player;

import me.bobthe28th.bday.Main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class EnemyHealthBar {

    private final Main plugin;
    private final BossBar enemyHealth;
    private LivingEntity enemy;
    private final double enemyHealthCooldownMax = 4.0;
    private double enemyHeathCooldown = 0.0;
    private boolean onEnemyHealthCooldown = false;

    public EnemyHealthBar(GamePlayer player, Main plugin) {
        this.plugin = plugin;
        enemyHealth = Bukkit.createBossBar("", BarColor.RED, BarStyle.SEGMENTED_10);
        enemyHealth.setVisible(false);
        enemyHealth.addPlayer(player.getPlayer());
    }

    public void updateEnemyHealth(LivingEntity entity) {
        updateEnemyHealth(entity, 0);
    }

    public void updateEnemyHealth(LivingEntity entity, double damage) {
        enemy = entity;
        AttributeInstance maxHealth = enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;
        double health = enemy.getHealth() - damage;
        double healthProgress = Math.min(1.0,Math.max(0.0,health / maxHealth.getValue()));
        enemyHealth.setProgress(healthProgress);
        enemyHealth.setTitle(enemy.getName());
        enemyHealth.setVisible(true);
    }

    public LivingEntity getEnemy() {
        return enemy;
    }

    private void setEnemyHealthCooldown() {
        onEnemyHealthCooldown = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                enemyHeathCooldown -= 0.1;
                enemyHeathCooldown = Math.round(enemyHeathCooldown*10.0)/10.0;
                if (enemyHeathCooldown <= 0) {
                    enemyHeathCooldown = 0;
                    enemy = null;
                    enemyHealth.setVisible(false);
                    onEnemyHealthCooldown = false;
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 2L, 2L);
    }

    public void startEnemyHealthCooldown() {
        enemyHeathCooldown = enemyHealthCooldownMax;
        if (!onEnemyHealthCooldown) {
            setEnemyHealthCooldown();
        }
    }
}
