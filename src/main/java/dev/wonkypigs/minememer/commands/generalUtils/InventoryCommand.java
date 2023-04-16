package dev.wonkypigs.minememer.commands.generalUtils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

@CommandAlias("mm|minememer")
public class InventoryCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("inventory|inv")
    public void openInventory(Player player) {
        openPlayerInventory(player, player);
    }

    @Syntax("<player>")
    @CommandCompletion("@AllPlayers")
    @Subcommand("inv|inventory")
    public void openInventoryOther(Player player, @Values("@AllPlayers") OfflinePlayer target) {
        openPlayerInventory(player, target);
    }

    public void openPlayerInventory(Player player, OfflinePlayer target) {
        Inventory inv;
        if (player != target) {
            inv = plugin.getServer().createInventory(new GUIHolders("inventory"), 45, plugin.lang.getString("other-inventory-menu-title")
                    .replace("&", "§")
                    .replace("{name}", target.getName())
            );
        } else {
            target = player;
            inv = plugin.getServer().createInventory(new GUIHolders("inventory"), 45, plugin.lang.getString("self-inventory-menu-title")
                    .replace("&", "§")
            );
        }
        OfflinePlayer finalTarget = target;
        if (!isPlayerRegistered(finalTarget)) {
            player.sendMessage(plugin.lang.getString("player-not-found")
                    .replace("&", "§"));
            return;
        }
        // get user's bank data
        CompletableFuture<Map<String, Integer>> future = CompletableFuture.supplyAsync(() -> {
            Map<String, Integer> itemList = getPlayerInventory(finalTarget);
            return itemList;
        }).whenComplete((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            }
        });

        future.thenAccept((itemList) -> {
            inventoryDisplaySetup(finalTarget, inv, itemList);
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inv));
        });
    }

    public void inventoryDisplaySetup(OfflinePlayer target, Inventory inv, Map<String, Integer> itemList) {
        // menu background
        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(plugin.menubg2);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
        for (int i = 36; i < 45; i++) {
            ItemStack item = new ItemStack(plugin.menubg2);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
        // player head item
        ItemStack skullItem = generatePlayerHead(target);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        skullMeta.setDisplayName(plugin.lang.getString("inventory-player-skull-name")
                .replace("&", "§")
                .replace("{name}", target.getName())
        );
        List<String> loreList = plugin.items.getStringList("inventory-player-skull-lore");
        ArrayList<String> lore = new ArrayList<>();
        for (String line: loreList) {
            lore.add(line
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(itemList.size()))
            );
        }
        skullMeta.setLore(lore);
        skullItem.setItemMeta(skullMeta);
        inv.setItem(4, skullItem);

        int curr_slot = 9;
        int max_slot = 35;
        for (Map.Entry<String, Integer> entry: itemList.entrySet()) {
            if (curr_slot > max_slot) {
                continue;
            }
            ItemStack invItem = setupInventoryItem(target, entry.getKey(), entry.getValue());
            inv.setItem(curr_slot, invItem);
            curr_slot++;
        }
    }
}
