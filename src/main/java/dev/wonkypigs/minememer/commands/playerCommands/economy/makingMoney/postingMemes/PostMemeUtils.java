package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.postingMemes;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.gettinThatBread;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickRandomNum;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.removePlayerItem;

public class PostMemeUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void postMeme(Player player, InventoryClickEvent e) {
        int failChance = plugin.economy.getInt("postmeme-failure-chance");
        // failure
        if (!(pickRandomNum(0, 101) > failChance)) {
            player.sendMessage(plugin.lang.getString("postmeme-failed-message")
                    .replace("&", "§")
                    .replace("{meme}", e.getCurrentItem().getItemMeta().getDisplayName())
                    .replace("{currency}", plugin.currencyName)
            );
            return;
        }
        // success
        List<Integer> giveAmountRange = plugin.economy.getIntegerList("postmeme-reward-range");
        int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();
        gettinThatBread(player, givenAmount);

        player.sendMessage(plugin.lang.getString("postmeme-done-message")
                .replace("&", "§")
                .replace("{meme}", e.getCurrentItem().getItemMeta().getDisplayName())
                .replace("{amount}", String.valueOf(givenAmount))
                .replace("{currency}", plugin.currencyName)
        );
        breakLaptop(player);
    }

    public static void breakLaptop(Player player) {
        int rodBreakChance = plugin.economy.getInt("laptop-break-chance");
        if ((!(pickRandomNum(0, 101) > rodBreakChance))) {
            player.sendMessage(plugin.lang.getString("item-broke")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.laptop.menu_name")
                            .replace("&", "§"))
            );
            removePlayerItem(player, "laptop", 1);
        }
    }
}
