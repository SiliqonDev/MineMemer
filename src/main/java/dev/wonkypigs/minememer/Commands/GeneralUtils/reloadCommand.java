package dev.wonkypigs.minememer.Commands.GeneralUtils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.command.CommandSender;

@CommandAlias("mm|minememer")
public class reloadCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("reload")
    @CommandPermission("mm.reload")
    public void reloadPlugin(CommandSender sender) {
        // TODO: RELOAD PLUGIN
    }
}
