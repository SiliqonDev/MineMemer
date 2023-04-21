package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Locale;

import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;
import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

@CommandAlias("mm|minememer")
public class GiveItemCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<player> <item> [<amount>]")
    @CommandCompletion("@AllPlayers @AllItems")
    @CommandPermission("mm.admin.giveitem")
    @Subcommand("giveitem")
    public void giveItemToPlayer(Player player, OfflinePlayer target, @Values("@AllItems") String itemName, @Default("1") int amount) {
        if (!isPlayerRegistered(target)) {
            player.sendMessage(plugin.lang.getString("player-not-found")
                    .replace("&", "§"));
            return;
        }
        if (!checkItemValidity(itemName.toLowerCase())) {
            player.sendMessage(plugin.lang.getString("invalid-item")
                    .replace("&", "§")
            );
        } else {
            givePlayerItem(target, itemName.toLowerCase(), amount);
            player.sendMessage(plugin.lang.getString("given-item")
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{item}", itemName.toUpperCase())
                    .replace("{player}", target.getName())
            );
        }
    }
}
