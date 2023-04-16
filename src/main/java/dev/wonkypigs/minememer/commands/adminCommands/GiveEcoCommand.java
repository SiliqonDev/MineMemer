package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;

@CommandPermission("mm.admin.giveeco")
@CommandAlias("mm|minememer")
public class GiveEcoCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<player> <amount>")
    @CommandCompletion("@AllPlayers")
    @Subcommand("giveeco|ecogive")
    public void giveEcoToPlayer(Player player, @Values("@AllPlayers") OfflinePlayer target, int amount) {
        if (!isPlayerRegistered(target)) {
            player.sendMessage(plugin.lang.getString("player-not-found")
                    .replace("&", "§"));
            return;
        }
        gettinThatBread(target, amount);
        player.sendMessage(plugin.lang.getString("given-eco")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(amount))
                .replace("{currency}", plugin.currencyName)
                .replace("{player}", target.getPlayer().getDisplayName())
        );
    }
}
