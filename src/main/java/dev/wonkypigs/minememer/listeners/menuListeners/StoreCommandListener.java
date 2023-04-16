package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.BuyMenu.*;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.SellMenu.*;

public class StoreCommandListener implements Listener {
    private static final MineMemer plugin = MineMemer.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && (((GUIHolders) holder).getType().equalsIgnoreCase("store"))) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(plugin.lang.getString("shop-sell-item-button")
                    .replace("&", "§"))) {
                openSellMenu((Player) e.getWhoClicked(), e.getInventory(), holder);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(plugin.lang.getString("shop-buy-item-button")
                    .replace("&", "§"))) {
                openBuyMenu((Player) e.getWhoClicked(), e.getInventory(), holder);
            }
        }
    }
}
