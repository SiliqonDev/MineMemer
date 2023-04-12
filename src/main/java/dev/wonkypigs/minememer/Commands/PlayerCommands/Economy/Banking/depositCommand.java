package dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.UUID;

import static dev.wonkypigs.minememer.Helpers.*;

@CommandAlias("mm|minememer")
public class depositCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("deposit|depo|dep")
    public void depositSelf(CommandSender sender, Command command, String label, String[] args) {
        if (!checkSenderIsPlayer(sender)) {
            return;
        } else if (args.length != 1) {
            sender.sendMessage(plugin.lang.getString("incorrect-usage")
                    .replace("&", "§")
                    .replace("{command}", "/deposit <amount>")
            );
            return;
        } else if (!args[0].matches("[0-9]+")) {
            sender.sendMessage(plugin.lang.getString("invalid-amount")
                    .replace("&", "§")
            );
            return;
        }
        Player player = (Player) sender;
        try {
            depositMoney(player, player.getUniqueId(), Integer.valueOf(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void depositMoney(Player player, UUID pID, Integer amt) throws Exception {
        // get user's bank data
        ResultSet results = grabBankData(pID).get();

        // set local variables for the data
        int purse = results.getInt("purse");
        int bankStored = results.getInt("bankStored");
        int bankLimit = results.getInt("bankLimit");

        if (amt > purse) {
            player.sendMessage(plugin.lang.getString("not-enough-money")
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
        updatePlayerBank(player, purse-amt, bankStored+amt, bankLimit);
    }
}
