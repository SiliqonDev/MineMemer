package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.begging;

import      co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;

@CommandAlias("mm|minememer")
public class BegCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("beg")
    public void begPlayer(Player player) {
        playerDidABeg(player);
    }

    public void playerDidABeg(Player player) {
        Random random = new Random();
        int failChance = plugin.economy.getInt("beg-failure-chance");
        // failure
        if (!(random.ints(0, 101).findFirst().getAsInt() > failChance)) {
            player.sendMessage(plugin.lang.getString("beg-failed-message")
                    .replace("&", "§")
                    .replace("{currency}", plugin.currencyName)
            );
            return;
        }

        // success
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
