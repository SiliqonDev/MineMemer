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

import static dev.wonkypigs.minememer.helpers.commandHelpers.SearchHelper.doSearch;


public class SearchMenuListener implements Listener {
    private static final MineMemer plugin = MineMemer.getInstance();

    NamespacedKey searchMenuItemKey = new NamespacedKey(plugin, plugin.searchMenuItemKeyName);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && ((GUIHolders) holder).getType().equalsIgnoreCase("search")) {
            if (e.getCurrentItem() == null) {
                return;
            }
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta.getPersistentDataContainer().has(searchMenuItemKey, PersistentDataType.STRING)) {
                player.closeInventory();
                doSearch(player, e);
            }
        }
    }
}
