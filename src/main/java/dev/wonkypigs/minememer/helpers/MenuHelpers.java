package dev.wonkypigs.minememer.helpers;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class MenuHelpers {

    public static void setMenuBackground(Inventory inv, Material material, int start, int stop) {
        for (int i = start; i < stop; i++) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
    }
    public static ItemStack generatePlayerHead(OfflinePlayer player, String displayName, ArrayList<String> lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(player);

        // display name
        skullMeta.setDisplayName(displayName);

        // lore
        skullMeta.setLore(lore);

        // done
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
