package dev.wonkypigs.minememer.commands.playerCommands.economy.store;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.*;

@CommandAlias("mm|minememer")
public class StoreCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("shop|store")
    public void openStore(Player player) {
        openStoreMenu(player);
    }

    public void openStoreMenu(Player player) {
        Inventory inv = plugin.getServer().createInventory(new GUIHolders("store"), 45, plugin.lang.getString("shop-menu-title")
                .replace("&", "§")
        );
        // menu background
        List<Material> matList = new ArrayList<>();
        matList.add(plugin.menubg2);
        matList.add(plugin.menubg);
        matList.add(plugin.menubg2);
        setMenuBackground(inv, matList);

        // buy button
        ItemStack buyButton = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta buyButtonMeta = buyButton.getItemMeta();
        buyButtonMeta.setDisplayName(plugin.lang.getString("shop-buy-item-button")
                .replace("&", "§")
        );
        buyButtonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        buyButton.setItemMeta(buyButtonMeta);
        inv.setItem(20, buyButton);

        // sell button
        ItemStack sellButton = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sellButtonMeta = sellButton.getItemMeta();
        sellButtonMeta.setDisplayName(plugin.lang.getString("shop-sell-item-button")
                .replace("&", "§")
        );
        sellButton.setItemMeta(sellButtonMeta);
        inv.setItem(24, sellButton);

        // balance display
        ItemStack balanceDisplay = new ItemStack(Material.DIAMOND);
        ItemMeta balanceDisplayMeta = balanceDisplay.getItemMeta();
        balanceDisplayMeta.setDisplayName(plugin.lang.getString("shop-balance-display")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(grabPlayerPurse(player)))
                .replace("{currency}", plugin.currencyName)
        );
        balanceDisplay.setItemMeta(balanceDisplayMeta);
        inv.setItem(40, balanceDisplay);

        player.openInventory(inv);
    }
}
