package dev.wonkypigs.minememer.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.entity.Player;

@CommandAlias("mm|minememer")
public class ReloadCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("reload")
    @CommandPermission("mm.admin.reload")
    public void reloadPlugin(Player player, @Default("no") String confirmation) {
        if (!confirmation.equalsIgnoreCase("confirm")) {
            player.sendMessage(plugin.lang.getString("confirm-reload")
                    .replace("&", "§"));
        } else {
            plugin.reloadPlugin();
            player.sendMessage(plugin.lang.getString("reload-success")
                    .replace("&", "§"));
        }
    }
}
