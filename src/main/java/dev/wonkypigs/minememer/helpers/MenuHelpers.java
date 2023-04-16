package dev.wonkypigs.minememer.helpers;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuHelpers {

    public static void setMenuBackground(Inventory inv, List<Material> materials) {
        if (inv.getSize() == 45) {
            for (int i = 0; i < 9; i++) {
                ItemStack item = new ItemStack(materials.get(0));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
            for (int i = 9; i < 36; i++) {
                ItemStack item = new ItemStack(materials.get(1));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
            for (int i = 36; i < 45; i++) {
                ItemStack item = new ItemStack(materials.get(2));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
        } else if (inv.getSize() == 27) {
            for (int i = 0; i < 27; i++) {
                ItemStack item = new ItemStack(materials.get(0));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
        }
    }
}
