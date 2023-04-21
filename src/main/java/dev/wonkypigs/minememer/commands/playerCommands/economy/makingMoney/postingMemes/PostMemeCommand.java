package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.postingMemes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static dev.wonkypigs.minememer.helpers.GeneralUtils.pickNRandom;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.getPlayerItemAmount;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.setMenuBackground;

@CommandAlias("mm|minememer")
public class PostMemeCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("postmeme")
    public void postAMeme(Player player) {
        if (getPlayerItemAmount(player, "laptop") == 0) {
            player.sendMessage(plugin.lang.getString("need-item")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.laptop.menu_name").replace("&", "§"))
            );
            return;
        }
        openPostmemeMenu(player);
    }

    public void openPostmemeMenu(Player player) {
        Inventory inv = plugin.getServer().createInventory(new GUIHolders("postmeme"), 27, plugin.lang.getString("postmeme-menu-title")
                .replace("&", "§")
        );

        // menu background
        setMenuBackground(inv, plugin.menubg, 0, 27, " ");

        // get search options
        List<String> postOptionList = plugin.economy.getStringList("postmeme-options");
        List<String> postOptions = pickNRandom(postOptionList, 3);

        // option 1 item
        ItemStack option1Item = new ItemStack(Material.PAPER);
        ItemMeta option1Meta = option1Item.getItemMeta();
        option1Meta.setDisplayName(postOptions.get(0));
        option1Item.setItemMeta(option1Meta);
        inv.setItem(11, option1Item);

        // option 2 item
        ItemStack option2Item = new ItemStack(Material.PAPER, 2);
        ItemMeta option2Meta = option2Item.getItemMeta();
        option2Meta.setDisplayName(postOptions.get(1));
        option2Item.setItemMeta(option2Meta);
        inv.setItem(13, option2Item);

        // option 3 item
        ItemStack option3Item = new ItemStack(Material.PAPER, 3);
        ItemMeta option3Meta = option3Item.getItemMeta();
        option3Meta.setDisplayName(postOptions.get(2));
        option3Item.setItemMeta(option3Meta);
        inv.setItem(15, option3Item);

        player.openInventory(inv);
    }
}
