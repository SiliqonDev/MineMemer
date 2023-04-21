package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.mining;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

import java.util.Random;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.getPlayerItemAmount;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.mining.MineCommandUtils.*;

@CommandAlias("mm|minememer")
public class MineCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("mine|mining")
    public void playerMine(Player player) {
        if (getPlayerItemAmount(player, "pickaxe") == 0) {
            player.sendMessage(plugin.lang.getString("need-item")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.pickaxe.menu_name").replace("&", "§"))
            );
            return;
        }
        playerDoneDidAMine(player);
    }

    public void playerDoneDidAMine(Player player) {
        Random random = new Random();
        int failChance = plugin.economy.getInt("mine-failure-chance");
        // failure
        if (!(random.ints(0, 101).findFirst().getAsInt() > failChance)) {
            player.sendMessage(plugin.lang.getString("mine-failed-message")
                    .replace("&", "§")
                    .replace("{currency}", plugin.currencyName)
            );
            return;
        }

        // success
        normalMine(player);
        breakPickaxe(player);
    }
}
