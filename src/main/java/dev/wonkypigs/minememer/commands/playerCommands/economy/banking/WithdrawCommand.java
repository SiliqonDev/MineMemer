package dev.wonkypigs.minememer.commands.playerCommands.economy.banking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;

@CommandAlias("mm|minememer")
public class WithdrawCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<amount>")
    @Subcommand("withdraw|withd|wd")
    public void depositSelf(Player player, int amount) {
        try {
            withdrawMoney(player, player.getUniqueId(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdrawMoney(Player player, UUID pID, Integer amt) throws Exception {
        // get user's bank data
        CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(() -> {
            ResultSet results = grabBankData(pID);
            return results;
        });
        future.thenAccept((results) -> {
            int purse = 0, bankStored = 0, bankLimit = 0;
            // set local variables for the data
            try {
                purse = results.getInt("purse");
                bankStored = results.getInt("bankStored");
                bankLimit = results.getInt("bankLimit");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (amt > bankStored) {
                player.sendMessage(plugin.lang.getString("not-enough-money-bank")
                        .replace("&", "§")
                        .replace("{amount}", String.valueOf(bankStored))
                        .replace("{required}", String.valueOf(amt))
                        .replace("{currency}", plugin.currencyName)
                );
                return;
            }
            player.sendMessage(plugin.lang.getString("withdrawn-from-bank")
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(amt))
                    .replace("{currency}", plugin.currencyName)
            );
            updatePlayerBank(player, purse+amt, bankStored-amt, bankLimit);
        });
    }
}
