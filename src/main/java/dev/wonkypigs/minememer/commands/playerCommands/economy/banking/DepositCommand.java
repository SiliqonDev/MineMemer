package dev.wonkypigs.minememer.commands.playerCommands.economy.banking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;

@CommandAlias("mm|minememer")
public class DepositCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<amount>")
    @Subcommand("deposit|depo|dep")
    public void depositSelf(Player player, int amount) {
        try {
            depositMoney(player, player.getUniqueId(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void depositMoney(Player player, UUID pID, Integer amt) {
        // get user's bank data
        CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(() -> {
            ResultSet results = grabBankData(pID);
            return results;
        });
        future.thenAccept((results) -> {
            int purse = 0, bankStored = 0, bankLimit = 0;
            try {
                // set local variables for the data
                purse = results.getInt("purse");
                bankStored = results.getInt("bankStored");
                bankLimit = results.getInt("bankLimit");

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (amt > purse) {
                player.sendMessage(plugin.lang.getString("not-enough-money-purse")
                        .replace("&", "§")
                        .replace("{amount}", String.valueOf(purse))
                        .replace("{required}", String.valueOf(amt))
                        .replace("{currency}", plugin.currencyName)
                );
                return;
            } else if ((bankStored + amt) > bankLimit) {
                player.sendMessage(plugin.lang.getString("cannot-surpass-bank-limit")
                        .replace("&", "§")
                        .replace("{limit}", String.valueOf(bankLimit))
                        .replace("{currency}", plugin.currencyName)
                );
                return;
            }
            player.sendMessage(plugin.lang.getString("deposited-to-bank")
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(amt))
                    .replace("{currency}", plugin.currencyName)
            );
            updatePlayerBank(player, purse-amt, bankStored+amt, bankLimit);
        });
    }
}
