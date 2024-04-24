package me.bobthe28th.bday.games.player;

import me.bobthe28th.bday.Main;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Regen {

    private boolean onHealCooldown = false;
    private final double healCooldownMax = 6.0;
    private double healCooldown = 0;

    private final Main plugin;
    private final GamePlayer player;

    public Regen(Main plugin, GamePlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    private void regen() {
        long rate = 10L;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (onHealCooldown || player == null) {
                    this.cancel();
                } else {
                    if (player.getPlayer().getHealth() < Objects.requireNonNull(player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue()) {
                        double healAmount = Math.min(Objects.requireNonNull(player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue(), player.getPlayer().getHealth() + 1);
                        ClientboundSetHealthPacket packet = new ClientboundSetHealthPacket((float) healAmount, 20, 5);
                        ((CraftPlayer)player.getPlayer()).getHandle().connection.send(packet);
                        player.getPlayer().setHealth(healAmount);
                    } else {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, rate);
    }

    public void startHealCooldown() {
        healCooldown = healCooldownMax;
        if (!onHealCooldown) {
            onHealCooldown = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    healCooldown -= 0.1;
                    healCooldown = Math.round(healCooldown * 10.0) / 10.0;
                    if (healCooldown <= 0) {
                        healCooldown = 0;
                        onHealCooldown = false;
                        regen();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 2L, 2L);
        }
    }
}
