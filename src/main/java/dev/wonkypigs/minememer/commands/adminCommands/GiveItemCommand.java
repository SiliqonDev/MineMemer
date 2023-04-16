package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

@CommandPermission("mm.admin.giveitem")
@CommandAlias("mm|minememer")
public class GiveItemCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<player> <item> [<amount>]")
    @CommandCompletion("@AllPlayers @AllItems")
    @Subcommand("giveitem|itemgive")
    public void giveItemToPlayer(Player player, @Values("@AllPlayers") OfflinePlayer target, @Values("@AllItems") String itemName, @Default("1") int amount) {
        if (!checkItemValidity(itemName)) {
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
