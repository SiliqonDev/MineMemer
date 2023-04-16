package dev.wonkypigs.minememer.helpers;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class EconomyUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static ResultSet grabBankData(UUID targetUUID) {
        ResultSet result = null;
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT purse, bankStored, bankLimit FROM mm_pdata WHERE uuid = ?");
            statement.setString(1, String.valueOf(targetUUID));
            result = statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int grabPlayerPurse(OfflinePlayer player) {
        int reply = 0;
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT purse FROM mm_pdata WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            reply = statement.executeQuery().getInt("purse");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reply;
    }

    public static void updatePlayerBank(Player player, Integer newPurse, Integer newBankStored, Integer newBankLimit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection()
                        .prepareStatement("UPDATE mm_pdata SET purse = ?, bankStored = ?, bankLimit = ? WHERE uuid = ?");
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

    public static void gettinThatBread(OfflinePlayer player, Integer amount) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection()
                        .prepareStatement("UPDATE mm_pdata SET purse = purse + ? WHERE uuid = ?");
                statement.setInt(1, amount);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void loosinThatBread(OfflinePlayer player, Integer amount) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection()
                        .prepareStatement("UPDATE mm_pdata SET purse = purse - ? WHERE uuid = ?");
                statement.setInt(1, amount);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
