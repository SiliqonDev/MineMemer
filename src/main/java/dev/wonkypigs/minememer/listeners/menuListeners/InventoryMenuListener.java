package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && (((GUIHolders) holder).getType().equalsIgnoreCase("inventory"))) {
            e.setCancelled(true);
        }
    }
}
