package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.hunting;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.givePlayerItem;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.removePlayerItem;

public class HuntCommandUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void doHunt(Player player, String animalCaught) {
        int failChance = plugin.economy.getInt("hunt-failure-chance");
        if ((!(pickRandomNum(0, 101) > failChance))) {
            // failure
            player.sendMessage(plugin.lang.getString("hunting-failed-message")
                    .replace("&", "§")
                    .replace("{animal}", animalCaught)
            );
        } else {
            // success
            player.sendMessage(plugin.lang.getString("hunting-done-message")
                    .replace("&", "§")
                    .replace("{animal}", animalCaught)
                    .replace("{item}", plugin.items.getString("items.animal_meat.menu_name").replace("&", "§"))
                    .replace("{amount}", "x1")
            );
            givePlayerItem(player, "animal_meat", 1);
        }
        breakRifle(player);
    }

    public static void breakRifle(Player player) {
        int rifleBreakChance = plugin.economy.getInt("hunting-rifle-break-chance");
        if ((!(pickRandomNum(0, 101) > rifleBreakChance))) {
            player.sendMessage(plugin.lang.getString("item-broke")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.hunting_rifle.menu_name")
                            .replace("&", "§"))
            );
            removePlayerItem(player, "hunting_rifle", 1);
        }
    }
}
