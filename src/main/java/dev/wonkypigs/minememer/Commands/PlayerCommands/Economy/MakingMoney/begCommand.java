package dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.MakingMoney;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.Helpers.checkSenderIsPlayer;
import static dev.wonkypigs.minememer.Helpers.gettinThatBread;

@CommandAlias("mm|minememer")
public class begCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("beg")
    public void begPlayer(CommandSender sender) {
        if (!checkSenderIsPlayer(sender)) {
            return;
        }
        Player player = (Player) sender;
        playerDidABeg(player);
    }

    public void playerDidABeg(Player player) {
        // get giver name
        List<String> giverNames = plugin.economy.getStringList("beg-giver-names");
        Random giverRandom = new Random();
        String giverName = giverNames.get(giverRandom.nextInt(giverNames.size()));

        // get given amount
        List<Integer> giveAmountRange = plugin.economy.getIntegerList("balance-command-give-range");
        int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();

        // do the give
        gettinThatBread(player, givenAmount);
        player.sendMessage(plugin.lang.getString("beg-done-message")
                .replace("&", "§")
                .replace("{giver}", giverName)
                .replace("{message}", "")
                .replace("{amount}", String.valueOf(givenAmount))
                .replace("{currency}", plugin.currencyName)
        );
    }
}
