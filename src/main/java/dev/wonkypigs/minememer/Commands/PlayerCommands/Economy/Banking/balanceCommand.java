package dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import static dev.wonkypigs.minememer.Helpers.*;

@CommandAlias("mm|minememer")
public class balanceCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("bank|balance")
    public void viewBankSelf(CommandSender sender) throws Exception {
        if (checkSenderIsPlayer(sender)) {
            return;
        }
        Player player = (Player) sender;
        showPlayerBank(player, player.getUniqueId());
    }

    @Syntax("<player>")
    @CommandCompletion("*")
    @Subcommand("bank|balance")
    public void viewBankOther(CommandSender sender, Command command, String label, String[] args) throws Exception {
        if (!checkSenderIsPlayer(sender)) {
            return;
        }
        Player player = (Player) sender;
        ResultSet tUUIDresult = getPlayerUUIDByName(args[0]).get();
        if (tUUIDresult.next()) {
            sendErrorToPlayer(player, plugin.lang.getString("player-not-found-error"));
            return;
        }
        UUID tUUID = UUID.fromString(tUUIDresult.getString("uuid"));
        showPlayerBank(player, tUUID);
    }

    public void showPlayerBank(Player player, UUID targetUUID) throws Exception {
        Inventory inv;
        OfflinePlayer target;
        if (player.getUniqueId().equals(targetUUID)) {
            target = Bukkit.getOfflinePlayer(targetUUID);
            inv = plugin.getServer().createInventory(null, 27, plugin.lang.getString("other-bank-menu-title").replace("&", "§").replace("{name}", target.getName()));
        } else {
            target = player;
            inv = plugin.getServer().createInventory(null, 27, plugin.lang.getString("self-bank-menu-title").replace("&", "§"));
        }
        // get user's bank data
        ResultSet results = grabBankData(targetUUID).get();

        // set local variables for the data
        int purse = results.getInt("purse");
        int bankStored = results.getInt("bankStored");
        int bankLimit = results.getInt("bankLimit");

        // menu background
        for (int i = 0; i < 27; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        // Purse item
        ItemStack balanceItem = new ItemStack(Material.PAPER);
        ItemMeta balanceMeta = balanceItem.getItemMeta();
        balanceMeta.setDisplayName((plugin.lang.getString("bank-purse-item-name"))
                .replace("&", "§")
                .replace("{purseMoney}", String.valueOf(purse))
                .replace("{currency}", plugin.currencyName)
        );
        balanceItem.setItemMeta(balanceMeta);
        inv.setItem(11, balanceItem);

        // Player head item
        ItemStack skullItem = generatePlayerHead(target);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        skullMeta.setDisplayName(plugin.lang.getString("bank-player-skull-item-name")
                .replace("&", "§")
        );
        ArrayList<String> lore = new ArrayList<>();
        lore.add(plugin.lang.getString("bank-player-skull-lore")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(purse+bankStored))
                .replace("{currency}", plugin.currencyName)
        );
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
        inv.setItem(15, bankItem);
        player.openInventory(inv);
    }
}
