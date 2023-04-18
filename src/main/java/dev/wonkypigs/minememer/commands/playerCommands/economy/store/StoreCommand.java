package dev.wonkypigs.minememer.commands.playerCommands.economy.store;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.*;

@CommandAlias("mm|minememer")
public class StoreCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("shop|store")
    public void openStore(Player player) {
        Inventory inv = plugin.getServer().createInventory(new GUIHolders("store"), 36, plugin.lang.getString("shop-menu-title")
                .replace("&", "§")
        );
        openStoreMenu(player, inv);
    }

    public static void openStoreMenu(Player player, Inventory inv) {
        // reset holder
        ((GUIHolders) inv.getHolder()).setType("store");

        // menu background
        setMenuBackground(inv, plugin.menubg, 0, 27, " ");
        setMenuBackground(inv, plugin.menubg2, 27, 36, " ");

        // buy button
        ItemStack buyButton = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta buyButtonMeta = buyButton.getItemMeta();
        buyButtonMeta.setDisplayName(plugin.lang.getString("shop-buy-item-button")
                .replace("&", "§")
        );
        buyButtonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        NamespacedKey buyButtonKey = new NamespacedKey(plugin, plugin.buyButtonKeyName);
        buyButtonMeta.getPersistentDataContainer().set(buyButtonKey, PersistentDataType.STRING, "yes");
        buyButton.setItemMeta(buyButtonMeta);
        inv.setItem(11, buyButton);

        // sell button
        ItemStack sellButton = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sellButtonMeta = sellButton.getItemMeta();
        sellButtonMeta.setDisplayName(plugin.lang.getString("shop-sell-item-button")
                .replace("&", "§")
        );
        NamespacedKey sellButtonKey = new NamespacedKey(plugin, plugin.sellButtonKeyName);
        sellButtonMeta.getPersistentDataContainer().set(sellButtonKey, PersistentDataType.STRING, "yes");
        sellButton.setItemMeta(sellButtonMeta);
        inv.setItem(15, sellButton);

        player.openInventory(inv);
    }
}
