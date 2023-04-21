package dev.wonkypigs.minememer.helpers;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;

public class MenuHelpers {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void setMenuBackground(Inventory inv, Material material, int start, int stop, String displayName) {
        for (int i = start; i < stop; i++) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
    }
    public static ItemStack generatePlayerHead(OfflinePlayer player, String displayName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(player);

        // display name
        skullMeta.setDisplayName(displayName);

        // done
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(plugin.HEADS_RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    public static ItemStack getCustomHead(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwnerProfile(getProfile(url));
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
