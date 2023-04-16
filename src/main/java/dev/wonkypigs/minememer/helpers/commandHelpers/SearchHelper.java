package dev.wonkypigs.minememer.helpers.commandHelpers;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.gettinThatBread;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;

public class SearchHelper {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void doSearch(Player player, InventoryClickEvent e) {
        int failChance = plugin.economy.getInt("search-failure-chance");
        // failure
        if (!(pickRandomNum(0, 101) > failChance)) {
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
