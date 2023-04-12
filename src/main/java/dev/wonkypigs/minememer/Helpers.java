package dev.wonkypigs.minememer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Helpers {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static boolean checkSenderIsPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }
        sender.sendMessage((plugin.lang.getString("must-be-a-player"))
                .replace("&", "§")
        );
        return false;
    }

    public static boolean senderHasPerms(Player p, String perm) {
        if (p.hasPermission(perm)) {
            return true;
        }
        p.sendMessage((plugin.lang.getString("no-perms-to-use"))
                .replace("&", "§")
        );
        return false;
    }

    public static CompletableFuture<ResultSet> getPlayerUUIDByName(String name) {
        CompletableFuture<ResultSet> data = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT uuid FROM playerdata WHERE name = ?");
                statement.setString(1, name);
                ResultSet results = statement.executeQuery();
                data.complete(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return data;
    }

    public static void sendErrorToPlayer(Player target, String error) {
        target.sendMessage("&8&m====================");
        target.sendMessage((plugin.lang.getString("error-main-message"))
                .replace("&", "§")
                .replace("{errorID}", error)
        );
        target.sendMessage("&8&m====================");
    }

    public static ItemStack generatePlayerHead(OfflinePlayer player) {
        ItemStack pHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) pHead.getItemMeta();
        headMeta.setOwningPlayer(player);
        pHead.setItemMeta(headMeta);

        return pHead;
    }

    public static CompletableFuture<ResultSet> grabBankData(UUID targetUUID) {
        CompletableFuture<ResultSet> data = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT purse, bankStored, bankLimit FROM pdata WHERE uuid = ?");
                statement.setString(1, String.valueOf(targetUUID));
                ResultSet resultSet = statement.executeQuery();
                data.complete(resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return data;
    }

    public static void updatePlayerBank(Player player, Integer newPurse, Integer newBankStored, Integer newBankLimit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection()
                        .prepareStatement("UPDATE pdata SET purse = ?, bankStored = ?, bankLimit = ? WHERE uuid = ?");
                statement.setInt(1, newPurse);
                statement.setInt(2, newBankStored);
                statement.setInt(3, newBankLimit);
                statement.setString(4, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void gettinThatBread(Player player, Integer amount) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection()
                        .prepareStatement("UPDATE pdata SET purse = purse + ? WHERE uuid = ?");
                statement.setInt(1, amount);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
