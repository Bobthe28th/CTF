package me.bobthe28th.bday.classes.items;

import io.netty.util.internal.UnstableApi;
import me.bobthe28th.bday.Main;
import me.bobthe28th.bday.games.player.GamePlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GameItem {
    private final Main plugin;
    private final String name;
    private Material mat;
    private ItemStack item = null;
    private final int amount = 1;
    private int customModel;
    private final GamePlayer owner;
    private final int defaultSlot;
    private int slot;

    public GameItem(Main plugin, String name, Material mat, Integer customModel, GamePlayer owner, int defaultSlot) {
        this.plugin = plugin;
        this.name = name;
        this.mat = mat;
        this.customModel = customModel;
        this.owner = owner;
        this.defaultSlot = defaultSlot;
        this.slot = this.defaultSlot;
    }

    public void onClickAction(PlayerInteractEvent event) {}
    public void onBlockPlace(BlockPlaceEvent event) {}
    public void onHold(PlayerItemHeldEvent event) {}
    public void onConsume(PlayerItemConsumeEvent event) {}

    public void displayCooldowns() {
        owner.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    public void setCustomModel(int customModel) {
        this.customModel = customModel;
    }

    public Material getMat() {
        return mat;
    }

    public void createItem() {
        if (item != null) {
            item.setAmount(0);
        }
        item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + name);
            meta.setCustomModelData(customModel);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "gameItem"), PersistentDataType.BOOLEAN, true);
            item.setItemMeta(meta);
        }
    }

    public ItemStack getItem() {
        if (item == null) createItem();
        return item;
    }

    public int getDefaultSlot() {
        return defaultSlot;
    }

    public int getSlot() {
        return slot;
    }

    public void remove() {
        if (item != null) item.setAmount(0);
    }

    @SuppressWarnings("UnstableApiUsage")
    public void setSlot(int newSlot) {
//        if (owner.getItem(newSlot) != null) { //if there is an item in the new slot
//            owner.getItem(newSlot).setSlot(owner.getItem(newSlot).getDefaultSlot()); //try to set the item to its default
//        }
//
//        ItemStack oldItem = owner.getPlayer().getInventory().getItem(slot);
//
//        if (oldItem != null) {
//            owner.getPlayer().getInventory().setItem(newSlot, oldItem);
//            oldItem.setAmount(0);
//        }
//        owner.getPlayer().updateInventory();
//        slot = newSlot;
    }
}
