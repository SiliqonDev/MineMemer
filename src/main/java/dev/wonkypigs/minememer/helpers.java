package dev.wonkypigs.minememer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.Yaml;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class helpers {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static CompletableFuture<ResultSet> getPlayerUUIDByName(String name) {
        CompletableFuture<ResultSet> data = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT uuid FROM mm_pdata WHERE name = ?");
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

    public static void gettinThatBread(Player player, Integer amount) {
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

    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new ArrayList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
    public static Integer pickRandomNum(int min, int max) {
        Random random = new Random();
        int num = random.ints(min, max).findFirst().getAsInt();
        return num;
    }
}
