package dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.MakingMoney;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

@CommandAlias("mm|minememer")
public class fishCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("fish|fishing")
    public void fishForFish(Player player) {
        // TODO: let the player fish for fish
    }
}
