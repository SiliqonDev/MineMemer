package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.fishing;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.*;

import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

public class FishCommandUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static List<String> getValidFishList() {
        List<String> fishList = new ArrayList<>();
        Set<String> itemNames = plugin.items.getConfigurationSection("items").getKeys(false);
        for (String item: itemNames) {
            if (plugin.items.getBoolean("items." + item + ".is_fish")) {
                fishList.add(item);
            }
        }
        return fishList;
    }

    public static String getFishingResult() {
        List<String> fishList = getValidFishList();
        List<String> chances = new ArrayList<>();
        for (String fish: fishList) {
            int fishChance = plugin.items.getInt("items." + fish + ".fish_obtain_chance");
            chances.addAll(Collections.nCopies(fishChance, fish));
        }
        Random random = new Random();
        return chances.get(random.nextInt(chances.size()));
    }
    public static void catchFish(Player player) {
        int failChance = plugin.economy.getInt("fishing-failure-chance");
        if ((!(pickRandomNum(0, 101) > failChance))) {
            // failure
            player.sendMessage(plugin.lang.getString("fishing-failed-message")
                    .replace("&", "§")
            );
        } else {
            // success
            String fishCaught = getFishingResult();
            player.sendMessage(plugin.lang.getString("fishing-done-message")
                    .replace("&", "§")
                    .replace("{fish}", plugin.items.getString("items." + fishCaught + ".menu_name")
                            .replace("&", "§")
                    )
            );
            givePlayerItem(player, fishCaught, 1);
        }
        breakRod(player);
    }

    public static void clickedWater(Player player) {
        int failChance = plugin.economy.getInt("treasure-failure-chance");
        if ((!(pickRandomNum(0, 101) > failChance))) {
            // failure
            player.sendMessage(plugin.lang.getString("fishing-water-failure")
                    .replace("&", "§")
            );
        } else {
            // success
            String treasureObtained = getTreasureResult();
            player.sendMessage(plugin.lang.getString("fishing-water-success")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items." + treasureObtained + ".menu_name")
                            .replace("&", "§")
                    )
            );
            givePlayerItem(player, treasureObtained, 1);
        }
    }

    public static void breakRod(Player player) {
        int rodBreakChance = plugin.economy.getInt("fishing-rod-break-chance");
        if ((!(pickRandomNum(0, 101) > rodBreakChance))) {
            player.sendMessage(plugin.lang.getString("item-broke")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.fishing_rod.menu_name")
                            .replace("&", "§"))
            );
            removePlayerItem(player, "fishing_rod", 1);
        }
    }
}
