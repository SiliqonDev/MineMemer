package dev.wonkypigs.minememer.commands.playerCommands.economy.banking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;

@CommandAlias("mm|minememer")
public class BalanceCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("balance")
    public void viewBalanceSelf(Player player) {
        showPlayerBalance(player, player);
    }

    @Syntax("<player>")
    @CommandCompletion("@AllPlayers")
    @Subcommand("balance")
    public void viewBalanceOther(Player player, OfflinePlayer target) {
        showPlayerBalance(player, target);
    }

    public void showPlayerBalance(Player player, OfflinePlayer target) {
        if (!isPlayerRegistered(target)) {
            player.sendMessage(plugin.lang.getString("player-not-found")
                    .replace("&", "§"));
            return;
        }
        // get user's balance
        CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(() -> {
            ResultSet results = grabBankData(target.getUniqueId());
            return results;
        }).whenComplete((results, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                player.sendMessage(plugin.lang.getString("something-went-wrong")
                        .replace("&", "§"));
            }
        });

        future.thenAccept((results) -> {
            int purse, bankStored, bankLimit;
            try {
                purse = results.getInt("purse");
                bankStored = results.getInt("bankStored");
                bankLimit = results.getInt("bankLimit");
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(plugin.lang.getString("something-went-wrong")
                        .replace("&", "§"));
                return;
            }

            String name = target.getName() + "'s";
            if (target == player) {
                name = "Your";
            }
            player.sendMessage(plugin.lang.getString("balance-message")
                    .replace("&", "§")
                    .replace("{name}", name)
                    .replace("{purseMoney}", String.valueOf(purse))
                    .replace("{bankMoney}", String.valueOf(bankStored))
                    .replace("{bankLimit}", String.valueOf(bankLimit))
                    .replace("{currency}", plugin.currencyName)
            );
        });
    }
}