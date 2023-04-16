package dev.wonkypigs.minememer.helpers;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class InventoryUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static Map<String, Integer> getPlayerInventory(OfflinePlayer player) {
        Map<String, Integer> itemList = new HashMap<>();
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM mm_inventory WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String item = result.getString("item");
                int amount = result.getInt("amount");
                if (!checkItemValidity(item)) {
                    removePlayerItem(player, item, amount);
                }
                itemList.put(item, amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
    public static void givePlayerItem(OfflinePlayer player, String item, int amount) {
        if (getPlayerItemAmount(player, item) != 0) {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE mm_inventory SET amount = amount + ? WHERE uuid = ? AND item = ?");
                statement.setInt(1, amount);
                statement.setString(2, player.getUniqueId().toString());
                statement.setString(3, item);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("INSERT INTO mm_inventory (uuid, item, amount) VALUES (?, ?, ?)");
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, item);
                statement.setInt(3, amount);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void removePlayerItem(OfflinePlayer player, String item, int amount) {
        if ((getPlayerItemAmount(player, item) - amount) != 0) {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE mm_inventory SET amount = amount - ? WHERE uuid = ? AND item = ?");
                statement.setInt(1, amount);
                statement.setString(2, player.getUniqueId().toString());
                statement.setString(3, item);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("DELETE FROM mm_inventory WHERE uuid = ? AND item = ?");
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, item);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static int getPlayerItemAmount(OfflinePlayer player, String itemName) {
        int ans = 0;
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT amount FROM mm_inventory WHERE uuid = ? AND item = ?");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, itemName);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                ans = results.getInt("amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }
    public static ItemStack setupInventoryItem(String itemName, int amount) {
        String ymlPath = "items." + itemName;

        // setup item stack
        if (amount > 64) {
            amount = 64;
        }
        ItemStack item = new ItemStack(Material.valueOf(plugin.items.getString(ymlPath + ".item_material")), amount);
        ItemMeta itemMeta = item.getItemMeta();
        // grab lore list
        List<String> loreList = plugin.items.getStringList(ymlPath + ".item_lore");

        // display name
        itemMeta.setDisplayName(plugin.items.getString(ymlPath + ".menu_name")
                .replace("&", "§")
        );
        // lore
        ArrayList<String> lore = new ArrayList<>();
        for (String line: loreList) {
            lore.add(line.replace("&", "§"));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static List<String> getValidItemList() {
        List<String> itemList = new ArrayList<>();
        Set<String> itemNames = plugin.items.getConfigurationSection("items").getKeys(false);
        for (String item: itemNames) {
            itemList.add(item.toUpperCase());
        }
        return itemList;
    }
    public static boolean checkItemValidity(String item) {
        Set<String> itemNames = plugin.items.getConfigurationSection("items").getKeys(false);
        if (itemNames.contains(item)) {
            return true;
        } else {
            return false;
        }
    }
}
