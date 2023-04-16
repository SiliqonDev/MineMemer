package dev.wonkypigs.minememer.listeners;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlayerJoinListener implements Listener {
    private static final MineMemer plugin = MineMemer.getInstance();

    @EventHandler
    public void onPlayerAsyncJoin(AsyncPlayerPreLoginEvent e) {
        if (!isRegistered(e)) {
            registerPlayer(e);
        }
    }

    private boolean isRegistered(AsyncPlayerPreLoginEvent e) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM mm_pdata WHERE uuid = ?");
            statement.setString(1,e.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void registerPlayer(AsyncPlayerPreLoginEvent e) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("INSERT INTO mm_pdata (uuid, name, purse, bankStored, bankLimit) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, e.getUniqueId().toString());
            statement.setString(2, e.getName());
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, plugin.economy.getInt("starting-bank-limit"));
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
