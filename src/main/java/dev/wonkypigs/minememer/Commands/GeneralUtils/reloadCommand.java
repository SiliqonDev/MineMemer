package dev.wonkypigs.minememer.Commands.GeneralUtils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandAlias("mm|minememer")
public class reloadCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("reload")
    @CommandPermission("mm.reload")
    public void reloadPlugin(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("&aUse &7'&c/reload confirm&7'&a to confirm reload!\n&cWarning: This is not recommended and could break the plugin!");
        } else if ((args.length > 1) || (!args[0].equalsIgnoreCase("confirm"))) {
            sender.sendMessage(plugin.lang.getString("incorrect-usage")
                    .replace("&", "§")
                    .replace("{command}", "/reload (confirm)")
            );
        } else {
            // TODO: reload plugin
        }
    }
}
