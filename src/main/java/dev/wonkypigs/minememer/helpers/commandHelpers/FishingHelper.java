package dev.wonkypigs.minememer.helpers.commandHelpers;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.*;

import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

public class FishingHelper {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static List<String> getValidFishList() {
        List<String> fishList = new ArrayList<>();
        List<String> registeredFishList = plugin.items.getStringList("fish_list");
        Set<String> itemNames = plugin.items.getConfigurationSection("items").getKeys(false);
        for (String item: itemNames) {
            if (registeredFishList.contains(item)) {
                fishList.add(item);
            }
        }
        return fishList;
    }

    public static String getFishingResult() {
        List<String> fishList = getValidFishList();
        List<String> chances = new ArrayList<>();
        for (String fish: fishList) {
            int fishChance = plugin.items.getInt("items." + fish + ".obtain_chance");
            chances.addAll(Collections.nCopies(fishChance, fish));
        }
        Random random = new Random();
        return chances.get(random.nextInt(chances.size()));
    }

    public static void catchFish(Player player) {
        int failChance = plugin.economy.getInt("fishing-failure-chance");
        // failure
        if ((!(pickRandomNum(0, 101) > failChance))) {
            player.closeInventory();
            player.sendMessage(plugin.lang.getString("fishing-failed-message")
                    .replace("&", "§")
            );
        } else {
            // success
            String fishCaught = getFishingResult();
            player.closeInventory();
            player.sendMessage(plugin.lang.getString("fishing-done-message")
                    .replace("&", "§")
                    .replace("{fish}", fishCaught.toUpperCase().replace("_", " "))
            );
            givePlayerItem(player, fishCaught, 1);
        }
        breakRod(player);
    }

    public static void breakRod(Player player) {
        int rodBreakChance = plugin.economy.getInt("fishing-rod-break-chance");
        if ((!(pickRandomNum(0, 101) > rodBreakChance))) {
            player.sendMessage(plugin.lang.getString("fishing-rod-broke")
                    .replace("&", "§")
            );
            removePlayerItem(player, "fishing_rod", 1);
        }
    }
}
