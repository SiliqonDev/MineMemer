package dev.wonkypigs.minememer.commands.playerCommands.economy.store;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.getPlayerItemAmount;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.setMenuBackground;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.StoreHelper.getSellableItemList;

public class SellMenu {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void openSellMenu(Player player, Inventory inv, InventoryHolder holder) {
        // change holder type
        ((GUIHolders) holder).setType("store_sell");

        // menu background
        setMenuBackground(inv, plugin.menubg, 0, 27, " ");

        // back to shop button
        ItemStack backButton = new ItemStack(Material.RED_DYE);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        backButtonMeta.setDisplayName(plugin.lang.getString("back-to-shop-button")
                .replace("&", "§")
        );
        NamespacedKey backButtonKey = new NamespacedKey(plugin, plugin.backButtonKeyName);
        backButtonMeta.getPersistentDataContainer().set(backButtonKey, PersistentDataType.STRING, "yes");
        backButton.setItemMeta(backButtonMeta);
        inv.setItem(27, backButton);

        setupSellMenu(player, inv);
    }

    public static void setupSellMenu(Player player, Inventory inv) {
        List<String> itemsForSale = getSellableItemList();
        int curr_slot = 0;
        int max_slot = 26;
        for (String itemForSale: itemsForSale) {
            if (curr_slot > max_slot) {
                break;
            }
            String ymlPath = "items." + itemForSale;
            List<String> loreList = plugin.items.getStringList(ymlPath + ".item_lore");

            ItemStack item = new ItemStack(Material.valueOf(plugin.items.getString(ymlPath + ".item_material")));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(plugin.items.getString(ymlPath + ".menu_name").replace("&", "§"));

            // lore
            ArrayList<String> lore = new ArrayList<>();
            for (String line : loreList) {
                lore.add(line.replace("&", "§"));
            }
            lore.add(" ");
            List<String> sellLore = plugin.lang.getStringList("item-sell-lore");
            for (String line: sellLore) {
                lore.add(line
                        .replace("&", "§")
                        .replace("{price}", String.valueOf(plugin.items.getInt(ymlPath + ".sell_price")))
                        .replace("{currency}", plugin.currencyName)
                        .replace("{amount}", String.valueOf(getPlayerItemAmount(player, itemForSale)))
                );
            }
            lore.add(" ");
            // extra lore
            List<String> extraLoreList = plugin.lang.getStringList("store-item-extra-lore");
            for (String line: extraLoreList) {
                lore.add(line.replace("&", "§"));
            }

            itemMeta.setLore(lore);

            // persistent data
            NamespacedKey itemKey = new NamespacedKey(plugin, plugin.validItemKeyName);
            itemMeta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, itemForSale);

            // glint and flags
            if (plugin.items.getBoolean(ymlPath + ".item_glow")) {
                item.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
            }
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            item.setItemMeta(itemMeta);
            inv.setItem(curr_slot, item);
            curr_slot++;
        }
    }
}
