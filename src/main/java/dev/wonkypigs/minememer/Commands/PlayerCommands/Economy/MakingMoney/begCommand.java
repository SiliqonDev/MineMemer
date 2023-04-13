package dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.MakingMoney;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.helpers.*;

@CommandAlias("mm|minememer")
public class begCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("beg")
    public void begPlayer(Player player) {
        playerDidABeg(player);
    }

    public void playerDidABeg(Player player) {
        if (pickRandomNum(1, 7) == 2) {
            player.sendMessage(plugin.lang.getString("beg-failed-message")
                    .replace("&", "§")
                    .replace("{currency}", plugin.currencyName)
            );
            return;
        }
        Random begRandom = new Random();

        // get giver name
        List<String> giverNames = plugin.economy.getStringList("beg-giver-names");
        String giverName = giverNames.get(begRandom.nextInt(giverNames.size()));

        // get giver message
        List<String> giverMessages = plugin.economy.getStringList("beg-giver-messages");
        String giverMessage = giverMessages.get(begRandom.nextInt(giverMessages.size())).replace("{currency}", plugin.currencyName);

        // get given amount
        List<Integer> giveAmountRange = plugin.economy.getIntegerList("beg-give-range");
        int givenAmount = new Random().ints(giveAmountRange.get(0), giveAmountRange.get(1)).findFirst().getAsInt();

        // do the give
        gettinThatBread(player, givenAmount);
        player.sendMessage(plugin.lang.getString("beg-done-message")
                .replace("&", "§")
                .replace("{giver}", giverName)
                .replace("{message}", giverMessage)
                .replace("{amount}", String.valueOf(givenAmount))
                .replace("{currency}", plugin.currencyName)
        );
    }
}
