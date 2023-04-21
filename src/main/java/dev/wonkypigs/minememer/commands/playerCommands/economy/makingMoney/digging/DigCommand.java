package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.digging;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.Random;

import static dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.digging.DigCommandUtils.*;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

@CommandAlias("mm|minememer")
public class DigCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("dig|digging")
    public void playerDig(Player player) {
        if (getPlayerItemAmount(player, "shovel") == 0) {
            player.sendMessage(plugin.lang.getString("need-item")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.shovel.menu_name").replace("&", "§"))
            );
            return;
        }
        playerDoneDidADig(player);
    }

    public void playerDoneDidADig(Player player) {
        Random random = new Random();
        int failChance = plugin.economy.getInt("dig-failure-chance");
        // failure
        if (!(random.ints(0, 101).findFirst().getAsInt() > failChance)) {
            player.sendMessage(plugin.lang.getString("dig-failed-message")
                    .replace("&", "§")
                    .replace("{currency}", plugin.currencyName)
            );
            return;
        }

        // success
        int treasureChance = plugin.economy.getInt("dig-treasure-chance");
        if (random.ints(0, 101).findFirst().getAsInt() > treasureChance) {
            dugTreasure(player);
            return;
        }
        // no treasure, normal dig
        normalDig(player);
        breakShovel(player);
    }
}
