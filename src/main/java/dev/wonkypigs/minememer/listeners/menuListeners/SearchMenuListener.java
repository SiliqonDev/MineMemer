package dev.wonkypigs.minememer.listeners.menuListeners;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;

import java.util.List;
import java.util.Random;

public class SearchMenuListener implements Listener {

    private static final MineMemer plugin = MineMemer.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIHolders && ((GUIHolders) holder).getType().equalsIgnoreCase("search")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getType().equals(Material.PAPER)) {
                e.getWhoClicked().closeInventory();
                Random random = new Random();
                int failChance = plugin.economy.getInt("search-failure-chance");
                // failure
                if (!(random.ints(0, 101).findFirst().getAsInt() > failChance)) {
                    player.sendMessage(plugin.lang.getString("search-failed-message")
                            .replace("&", "§")
                            .replace("{place}", e.getCurrentItem().getItemMeta().getDisplayName())
                            .replace("{currency}", plugin.currencyName)
                    );
                    return;
                }
                // success
                List<Integer> giveAmountRange = plugin.economy.getIntegerList("search-doing-range");
                int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();
                gettinThatBread(player, givenAmount);

                player.sendMessage(plugin.lang.getString("search-done-message")
                        .replace("&", "§")
                        .replace("{place}", e.getCurrentItem().getItemMeta().getDisplayName())
                        .replace("{amount}", String.valueOf(givenAmount))
                        .replace("{currency}", plugin.currencyName)
                );
            }
        }
    }
}
