package dev.wonkypigs.minememer.commands.playerCommands.economy.banking;

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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;

@CommandAlias("mm|minememer")
public class BalanceCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("bank|balance")
    public void viewBankSelf(Player player) {
        showPlayerBank(player, player);
    }

    @Syntax("<player>")
    @CommandCompletion("@AllPlayers")
    @Subcommand("bank|balance")
    public void viewBankOther(Player player, @Values("@AllPlayers") OfflinePlayer target) {
        showPlayerBank(player, target);
    }

    public void showPlayerBank(Player player, OfflinePlayer target) {
        Inventory inv;
        if (player != target) {
            inv = plugin.getServer().createInventory(new GUIHolders("bank"), 36, plugin.lang.getString("other-bank-menu-title")
                    .replace("&", "§")
                    .replace("{name}", target.getName())
            );
        } else {
            target = player;
            inv = plugin.getServer().createInventory(new GUIHolders("bank"), 36, plugin.lang.getString("self-bank-menu-title")
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
        CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(() -> {
            ResultSet results = grabBankData(finalTarget.getUniqueId());
            return results;
        }).whenComplete((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            }
        });


        future.thenAccept((results) -> {
            bankDisplaySetup(finalTarget, inv, results);
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inv));
        });
    }

    public void bankDisplaySetup(OfflinePlayer target, Inventory inv, ResultSet results) {
        // set local variables for the data
        int purse = 0, bankStored = 0, bankLimit = 0;
        try {
            purse = results.getInt("purse");
            bankStored = results.getInt("bankStored");
            bankLimit = results.getInt("bankLimit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // menu background
        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(plugin.menubg2);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
        for (int i = 9; i < 36; i++) {
            ItemStack item = new ItemStack(plugin.menubg);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        // Player head item
        ItemStack skullItem = generatePlayerHead(target);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        skullMeta.setDisplayName(plugin.lang.getString("bank-player-skull-name")
                .replace("&", "§")
                .replace("{name}", target.getName())
        );
        List<String> loreList = plugin.items.getStringList("bank-player-skull-lore");
        ArrayList<String> lore = new ArrayList<>();
        for (String line: loreList) {
            lore.add(line
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(purse+bankStored))
                    .replace("{currency}", plugin.currencyName)
            );
        }
        skullMeta.setLore(lore);
        skullItem.setItemMeta(skullMeta);
        inv.setItem(4, skullItem);

        // Bank item
        ItemStack bankItem = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta bankMeta = bankItem.getItemMeta();
        bankMeta.setDisplayName(plugin.lang.getString("bank-bank-item-name")
                .replace("&", "§")
                .replace("{bankMoney}", String.valueOf(bankStored))
                .replace("{bankLimit}", String.valueOf(bankLimit))
                .replace("{currency}", plugin.currencyName)
        );
        bankItem.setItemMeta(bankMeta);
        inv.setItem(20, bankItem);

        // Purse item
        ItemStack balanceItem = new ItemStack(Material.DIAMOND);
        ItemMeta balanceMeta = balanceItem.getItemMeta();
        balanceMeta.setDisplayName((plugin.lang.getString("bank-purse-item-name"))
                .replace("&", "§")
                .replace("{currency}", plugin.currencyName)
                .replace("{purseMoney}", String.valueOf(purse))
        );
        balanceItem.setItemMeta(balanceMeta);
        inv.setItem(24, balanceItem);
    }
}