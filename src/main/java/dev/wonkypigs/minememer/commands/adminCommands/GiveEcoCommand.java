package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static dev.wonkypigs.minememer.helpers.EconomyUtils.*;

@CommandPermission("mm.admin.giveeco")
@CommandAlias("mm|minememer")
public class GiveEcoCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Syntax("<player> <amount>")
    @CommandCompletion("@AllPlayers")
    @Subcommand("giveeco|ecogive")
    public void giveEcoToPlayer(Player player, @Values("@AllPlayer") OfflinePlayer target, int amount) {
        gettinThatBread(target, amount);
        player.sendMessage(plugin.lang.getString("given-eco")
                .replace("&", "§")
                .replace("{amount}", String.valueOf(amount))
                .replace("{currency}", plugin.currencyName)
                .replace("{player}", target.getPlayer().getDisplayName())
        );
    }
}
