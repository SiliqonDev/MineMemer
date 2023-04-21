package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.digging;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.gettinThatBread;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

public class DigCommandUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void dugTreasure(Player player) {
        String treasureObtained = getTreasureResult();
        player.sendMessage(plugin.lang.getString("dig-treasure-success")
                .replace("&", "§")
                .replace("{item}", plugin.items.getString("items." + treasureObtained + ".menu_name")
                        .replace("&", "§")
                )
        );
        givePlayerItem(player, treasureObtained, 1);
    }

    public static void normalDig(Player player) {
        // get given amount
        List<Integer> giveAmountRange = plugin.economy.getIntegerList("dig-give-range");
        int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();

        // do the give
        gettinThatBread(player, givenAmount);
        player.sendMessage(plugin.lang.getString("dig-done-message")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(givenAmount))
                .replace("{currency}", plugin.currencyName)
        );
    }

    public static void breakShovel(Player player) {
        int shovelBreakChance = plugin.economy.getInt("shovel-break-chance");
        if ((!(pickRandomNum(0, 101) > shovelBreakChance))) {
            player.sendMessage(plugin.lang.getString("item-broke")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.shovel.menu_name")
                            .replace("&", "§"))
            );
            removePlayerItem(player, "shovel", 1);
        }
    }
}
