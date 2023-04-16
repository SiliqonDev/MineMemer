package dev.wonkypigs.minememer.helpers;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneralUtils {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static boolean isPlayerRegistered(OfflinePlayer player) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM mm_pdata WHERE uuid = ?");
            statement.setString(1,player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static ItemStack generatePlayerHead(OfflinePlayer player) {
        ItemStack pHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) pHead.getItemMeta();
        headMeta.setOwningPlayer(player);
        pHead.setItemMeta(headMeta);

        return pHead;
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
