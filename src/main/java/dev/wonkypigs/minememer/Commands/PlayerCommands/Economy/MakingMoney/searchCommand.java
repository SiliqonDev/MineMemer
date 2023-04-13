package dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.MakingMoney;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static dev.wonkypigs.minememer.helpers.checkSenderIsPlayer;
import static dev.wonkypigs.minememer.helpers.pickNRandom;

@CommandAlias("mm|minememer")
public class searchCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("search")
    public void searchForLoot(CommandSender sender) {
        if (!checkSenderIsPlayer(sender)) {
            return;
        }
        Player player = (Player) sender;
        openSearchMenu(player);
    }

    public void openSearchMenu(Player player) {
        Inventory menu = plugin.getServer().createInventory(null, 27, plugin.lang.getString("search-menu-title")
                .replace("&", "§")
        );

        // get search options
        List<String> searchOptionList = plugin.economy.getStringList("search-doing-places");
        List<String> searchOptions = pickNRandom(searchOptionList, 3);

        // menu background
        for (int i = 0; i < 27; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("");
            item.setItemMeta(meta);
            menu.setItem(i, item);
        }

        // search option 1 item
        ItemStack option1Item = new ItemStack(Material.PAPER);
        ItemMeta option1Meta = option1Item.getItemMeta();
        option1Meta.setDisplayName(searchOptions.get(0));
        option1Item.setItemMeta(option1Meta);
        menu.setItem(11, option1Item);

        // search option 2 item
        ItemStack option2Item = new ItemStack(Material.PAPER, 2);
        ItemMeta option2Meta = option2Item.getItemMeta();
        option2Meta.setDisplayName(searchOptions.get(1));
        option2Item.setItemMeta(option2Meta);
        menu.setItem(13, option2Item);

        // search option 3 item
        ItemStack option3Item = new ItemStack(Material.PAPER, 3);
        ItemMeta option3Meta = option3Item.getItemMeta();
        option3Meta.setDisplayName(searchOptions.get(2));
        option3Item.setItemMeta(option3Meta);
        menu.setItem(15, option3Item);

        player.openInventory(menu);
    }
}
