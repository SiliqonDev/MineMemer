package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Random;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

public class FishingMenuListener implements Listener {

    private static final MineMemer plugin = MineMemer.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && ((GUIHolders) holder).getType().equalsIgnoreCase("fishing")) {
            if (!e.getCurrentItem().getType().equals(Material.TROPICAL_FISH)) {
                e.setCancelled(true);
                return;
            }
            Player player = (Player) e.getWhoClicked();
            Random random = new Random();
            int failChance = plugin.economy.getInt("fishing-failure-chance");
            int rodBreakChance = plugin.economy.getInt("fishing-rod-break-chance");
            // failure
            if (!(random.ints(0, 101).findFirst().getAsInt() > failChance)) {
                player.closeInventory();
                player.sendMessage(plugin.lang.getString("fishing-failed-message")
                        .replace("&", "§")
                );
            } else {
                // success
                player.closeInventory();
                player.sendMessage(plugin.lang.getString("fishing-done-message")
                        .replace("&", "§")
                        .replace("{fish}", "")
                );
                givePlayerItem(player, "fish", 1);
            }
            if (!(random.ints(0, 101).findFirst().getAsInt() > rodBreakChance)) {
                player.sendMessage(plugin.lang.getString("fishing-rod-broke")
                        .replace("&", "§")
                );
                removePlayerItem(player, "fishing_rod", 1);
            }
        }
    }
}
