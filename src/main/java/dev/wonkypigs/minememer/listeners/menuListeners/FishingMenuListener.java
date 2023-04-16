package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import static dev.wonkypigs.minememer.helpers.commandHelpers.FishingHelper.catchFish;

public class FishingMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && ((GUIHolders) holder).getType().equalsIgnoreCase("fishing")) {
            if (!e.getCurrentItem().getType().equals(Material.TROPICAL_FISH)) {
                e.setCancelled(true);
                return;
            }
            Player player = (Player) e.getWhoClicked();
            catchFish(player);
        }
    }
}
