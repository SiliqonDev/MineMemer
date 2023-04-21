package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.mining;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.gettinThatBread;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

public class MineCommandUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void normalMine(Player player) {
        // get given amount
        List<Integer> giveAmountRange = plugin.economy.getIntegerList("mine-give-range");
        int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();

        // do the give
        gettinThatBread(player, givenAmount);
        player.sendMessage(plugin.lang.getString("mine-done-message")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(givenAmount))
                .replace("{currency}", plugin.currencyName)
        );
    }

    public static void breakPickaxe(Player player) {
        int shovelBreakChance = plugin.economy.getInt("pickaxe-break-chance");
        if ((!(pickRandomNum(0, 101) > shovelBreakChance))) {
            player.sendMessage(plugin.lang.getString("item-broke")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.pickaxe.menu_name")
                            .replace("&", "§"))
            );
            removePlayerItem(player, "pickaxe", 1);
        }
    }
}
