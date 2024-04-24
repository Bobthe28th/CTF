package me.bobthe28th.bday.classes.items.itemtypes;

import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.classes.items.GameItem;
import me.bobthe28th.bday.games.player.GamePlayer;
import org.bukkit.Material;

public class StackCooldownItem extends GameItem {
    private double cooldown = 0;
    private final double cdMax;
    private final String cdName;
    private final Material cdItem;

    public StackCooldownItem(Main plugin, String itemName, Material item, Integer customModel, String cdName, double cdMax, Material cdItem, GamePlayer owner, Integer defaultSlot) {
        super(plugin, itemName, item, customModel, owner, defaultSlot);
        this.cdName = cdName;
        this.cdMax = cdMax;
        this.cdItem = cdItem;
    }
}
