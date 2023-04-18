package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static dev.wonkypigs.minememer.helpers.commandHelpers.FishingHelper.*;

public class FishingMenuListener implements Listener {
    private static final MineMemer plugin = MineMemer.getInstance();
    NamespacedKey fishItemKey = new NamespacedKey(plugin, plugin.fishItemKeyName);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && ((GUIHolders) holder).getType().equalsIgnoreCase("fishing")) {
            if (e.getCurrentItem() == null) {
                return;
            }
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta.getPersistentDataContainer().has(fishItemKey, PersistentDataType.STRING)) {
                catchFish(player);
                player.closeInventory();
            } /* else if (e.getCurrentItem().getType().equals(Material.BLUE_STAINED_GLASS_PANE)) {
                clickedWater(player);
                player.closeInventory();
            } */ // TODO: fix this
        }
    }
}
