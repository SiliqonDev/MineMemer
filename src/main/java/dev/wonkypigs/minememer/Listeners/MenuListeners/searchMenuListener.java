package dev.wonkypigs.minememer.Listeners.MenuListeners;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import static dev.wonkypigs.minememer.helpers.*;

import java.util.List;
import java.util.Random;

public class searchMenuListener implements Listener {

    private static final MineMemer plugin = MineMemer.getInstance();

    @EventHandler
    public void searchMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!e.getView().getTitle().equals(plugin.lang.getString("search-menu-title").replace("&", "§"))) {
            e.setCancelled(true);
        } else if (e.getCurrentItem().getType().equals(Material.PAPER)) {
            e.getView().close();
            if (pickRandomNum(1, 10) == 3) {
                player.sendMessage(plugin.lang.getString("search-failed-message")
                        .replace("&", "§")
                        .replace("{place}", e.getCurrentItem().getItemMeta().getDisplayName())
                        .replace("{currency}", plugin.currencyName)
                );
                return;
            }
            // get given amount
            List<Integer> giveAmountRange = plugin.economy.getIntegerList("search-doing-range");
            int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();
            gettinThatBread(player, givenAmount);

            player.sendMessage(plugin.lang.getString("search-done-message")
                    .replace("&", "§")
                    .replace("{place}", e.getCurrentItem().getItemMeta().getDisplayName())
                    .replace("{amount}", String.valueOf(givenAmount))
                    .replace("{currency}", plugin.currencyName)
            );
        } else {
            e.setCancelled(true);
        }
    }
}
