package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;
import static dev.wonkypigs.minememer.helpers.GeneralUtils.*;

@CommandAlias("mm|minememer")
public class TakeEcoCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<player> <amount>")
    @CommandCompletion("@AllPlayers")
    @CommandPermission("mm.admin.takeeco")
    @Subcommand("takeeco|ecotake")
    public void takeEcoFromPlayer(Player player, OfflinePlayer target, int amount) {
        if (!isPlayerRegistered(target)) {
            player.sendMessage(plugin.lang.getString("player-not-found")
                    .replace("&", "§"));
            return;
        }
        int targetPurse = grabPlayerPurse(target);
        if (targetPurse < amount) {
            player.sendMessage(plugin.lang.getString("player-does-not-have-that-much-eco")
                    .replace("&", "§")
                    .replace("{player}", target.getName())
                    .replace("{currency}", plugin.currencyName)
                    .replace("{amount}", String.valueOf(targetPurse))
                    .replace("{required}", String.valueOf(amount))
            );
        } else {
            loosinThatBread(target, amount);
            player.sendMessage(plugin.lang.getString("taken-eco")
                    .replace("&", "§")
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{currency}", plugin.currencyName)
                    .replace("{player}", target.getName())
            );
        }
    }
}
