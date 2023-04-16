package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;

@CommandPermission("mm.admin.takeitem")
@CommandAlias("mm|minememer")
public class TakeItemCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<player> <item> [<amount>]")
    @CommandCompletion("@AllPlayers @AllItems")
    @Subcommand("takeitem|itemtake")
    public void giveItemToPlayer(Player player, @Values("@AllPlayers") OfflinePlayer target, @Values("@AllItems") String itemName, @Default("1") int amount) {
        if (!checkItemValidity(itemName)) {
            player.sendMessage(plugin.lang.getString("invalid-item")
                    .replace("&", "§")
            );
        } else if (getItemAmount(target, itemName) < amount) {
            player.sendMessage(plugin.lang.getString("player-does-not-have-item")
                    .replace("&", "§")
                    .replace("{player}", target.getName())
                    .replace("{amount}", String.valueOf(amount))
            );
        } else {
            removePlayerItem(target, itemName.toLowerCase(), amount);
            player.sendMessage(plugin.lang.getString("taken-item")
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{item}", itemName.toUpperCase())
                    .replace("{player}", target.getName())
            );
        }
    }
}
