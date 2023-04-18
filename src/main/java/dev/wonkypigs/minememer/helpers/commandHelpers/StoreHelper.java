package dev.wonkypigs.minememer.helpers.commandHelpers;

import dev.wonkypigs.minememer.MineMemer;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.BuyMenu.setupBuyMenu;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.store.SellMenu.setupSellMenu;
import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

public class StoreHelper {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static List<String> getBuyableItemList() {
        List<String> buyableList = new ArrayList<>();
        Set<String> itemNames = plugin.items.getConfigurationSection("items").getKeys(false);
        for (String item: itemNames) {
            if (plugin.items.getBoolean("items." + item + ".buyable")) {
                buyableList.add(item);
            }
        }
        return buyableList;
    }
    public static List<String> getSellableItemList() {
        List<String> itemList = new ArrayList<>();
        Set<String> itemNames = plugin.items.getConfigurationSection("items").getKeys(false);
        for (String item: itemNames) {
            if (plugin.items.getBoolean("items." + item + ".sellable")) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    public static void buyItem(Player player, ItemStack item, Inventory inv, int amount) {
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, plugin.validItemKeyName);
        String itemName = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        // buying
        int cost = plugin.items.getInt("items." + itemName + ".buy_price")*amount;
        int purse = grabPlayerPurse(player);
        if (purse < cost) {
            player.closeInventory();
            player.sendMessage(plugin.lang.getString("not-enough-money-purse")
                    .replace("&", "§")
                    .replace("{currency}", plugin.currencyName)
                    .replace("{amount}", String.valueOf(purse))
                    .replace("{required}", String.valueOf(cost))
            );
            return;
        }
        loosinThatBread(player, cost);
        givePlayerItem(player, itemName, amount);
        player.sendMessage(plugin.lang.getString("bought-item")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(amount))
                .replace("{item}", plugin.items.getString("items." + itemName + ".menu_name")
                        .replace("&", "§")
                )
                .replace("{price}", String.valueOf(cost))
                .replace("{currency}", plugin.currencyName)
        );
        setupBuyMenu(inv);
    }
    public static void sellItem(Player player, ItemStack item, Inventory inv, int amount) {
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, plugin.validItemKeyName);
        String itemName = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        // selling
        int available = getPlayerItemAmount(player, itemName);
        int sellPrice = plugin.items.getInt("items." + itemName + ".sell_price")*amount;
        if (available < amount) {
            player.sendMessage(plugin.lang.getString("not-enough-item")
                    .replace("&", "§")
            );
            return;
        }
        gettinThatBread(player, sellPrice);
        removePlayerItem(player, itemName, amount);
        player.sendMessage(plugin.lang.getString("sold-item")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(amount))
                .replace("{item}", plugin.items.getString("items." + itemName + ".menu_name")
                        .replace("&", "§")
                )
                .replace("{price}", String.valueOf(sellPrice))
                .replace("{currency}", plugin.currencyName)
        );
        setupSellMenu(player, inv);
    }
    public static void openStoreAmountInput(Player player, ItemStack item, Inventory inv, String type) {
        new AnvilGUI.Builder()
                .onComplete((completion) -> {
                    if ((!completion.getText().isEmpty()) && completion.getText().matches("[0-9]+")) {
                        if (type.equalsIgnoreCase("buy")) {
                            buyItem(player, item, inv, Integer.parseInt(completion.getText()));
                        } else if (type.equalsIgnoreCase("sell")) {
                            sellItem(player, item, inv, Integer.parseInt(completion.getText()));
                        } else {
                            player.sendMessage(plugin.lang.getString("something-went-wrong")
                                    .replace("&", "§")
                            );
                        }
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    } else {
                        player.sendMessage(plugin.lang.getString("invalid-amount")
                                .replace("&", "§")
                        );
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    }
                })
                .title("Enter an amount")
                .text("Enter amount here")
                .plugin(plugin)
                .open(player);
    }
}
