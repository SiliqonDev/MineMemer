package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.BuyMenu.*;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.SellMenu.*;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.StoreCommand.openStoreMenu;
import static dev.wonkypigs.minememer.helpers.commandHelpers.StoreHelper.*;

public class StoreMenuListener implements Listener {
    private static final MineMemer plugin = MineMemer.getInstance();
    NamespacedKey sellButtonKey = new NamespacedKey(plugin, plugin.sellButtonKeyName);
    NamespacedKey buyButtonKey = new NamespacedKey(plugin, plugin.buyButtonKeyName);
    NamespacedKey backButtonKey = new NamespacedKey(plugin, plugin.backButtonKeyName);
    NamespacedKey validItemKey = new NamespacedKey(plugin, plugin.validItemKeyName);

    @EventHandler
    public void onStoreClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }
        e.setCancelled(true);
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && (((GUIHolders) holder).getType().equalsIgnoreCase("store"))) {
            if (e.getCurrentItem() == null) {
                return;
            }
            e.setCancelled(true);
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta.getPersistentDataContainer().has(buyButtonKey, PersistentDataType.STRING)) {
                openBuyMenu(e.getInventory(), holder);
            } else if (meta.getPersistentDataContainer().has(sellButtonKey, PersistentDataType.STRING)) {
                openSellMenu((Player) e.getWhoClicked(), e.getInventory(), holder);
            }
        }
    }

    @EventHandler
    public void onStoreBuyClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }
        e.setCancelled(true);
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && (((GUIHolders) holder).getType().equalsIgnoreCase("store_buy"))) {
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            Player player = (Player) e.getWhoClicked();
            if (meta.getPersistentDataContainer().has(validItemKey, PersistentDataType.STRING)) {
                if (e.getClick().isRightClick()) {
                    openStoreAmountInput(player, e.getCurrentItem(), e.getInventory(), "BUY");
                } else {
                    buyItem(player, e.getCurrentItem(), e.getInventory(), 1);
                }
            } else if (meta.getPersistentDataContainer().has(backButtonKey, PersistentDataType.STRING)) {
                openStoreMenu(player, e.getInventory());
            }
        }
    }

    @EventHandler
    public void onStoreSellClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }
        e.setCancelled(true);
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && (((GUIHolders) holder).getType().equalsIgnoreCase("store_sell"))) {
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            Player player = (Player) e.getWhoClicked();
            if (meta.getPersistentDataContainer().has(validItemKey, PersistentDataType.STRING)) {
                if (e.getClick().isRightClick()) {
                    openStoreAmountInput(player, e.getCurrentItem(), e.getInventory(), "SELL");
                } else {
                    sellItem(player, e.getCurrentItem(), e.getInventory(), 1);
                }
            } else if (meta.getPersistentDataContainer().has(backButtonKey, PersistentDataType.STRING)) {
                openStoreMenu(player, e.getInventory());
            }
        }
    }
}
