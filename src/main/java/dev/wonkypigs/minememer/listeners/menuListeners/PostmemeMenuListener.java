package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import static dev.wonkypigs.minememer.helpers.commandHelpers.PostmemeHelper.*;

public class PostmemeMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && ((GUIHolders) holder).getType().equalsIgnoreCase("postmeme")) {
            if (e.getCurrentItem() == null) {
                return;
            }
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getType().equals(Material.PAPER)) {
                e.getWhoClicked().closeInventory();
                postMeme(player, e);
            }
        }
    }
}
