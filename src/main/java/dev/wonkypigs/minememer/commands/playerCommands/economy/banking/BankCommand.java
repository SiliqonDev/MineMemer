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

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.grabBankData;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.isPlayerRegistered;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.*;

public class BankCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("bank")
    public void viewBankSelf(Player player) {
        showPlayerBank(player, player);
    }

    @Syntax("<player>")
    @CommandCompletion("@AllPlayers")
    @Subcommand("bank")
    public void viewBankOther(Player player, OfflinePlayer target) {
        showPlayerBank(player, target);
    }

    public void showPlayerBank(Player player, OfflinePlayer target) {
        if (!isPlayerRegistered(target)) {
            player.sendMessage(plugin.lang.getString("player-not-found")
                    .replace("&", "§"));
            return;
        }
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
        // get user's bank data
        CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(() -> {
            ResultSet results = grabBankData(finalTarget.getUniqueId());
            return results;
        }).whenComplete((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                player.sendMessage(plugin.lang.getString("something-went-wrong")
                        .replace("&", "§"));
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
        setMenuBackground(inv, plugin.menubg2, 0, 9, " ");
        setMenuBackground(inv, plugin.menubg, 9, 36, " ");

        // Player head item
        String skullDisplayName = plugin.lang.getString("bank-player-skull-name")
                .replace("&", "§")
                .replace("{name}", target.getName());

        ItemStack skullItem = generatePlayerHead(target, skullDisplayName);
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
