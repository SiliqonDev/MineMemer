package dev.wonkypigs.minememer.mobs;

import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class DogeSpawnManager implements Listener {
    private static final MineMemer plugin = MineMemer.getInstance();
    NamespacedKey dogeKey = new NamespacedKey(plugin, plugin.dogeKeyName);

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        if (e.getEntity().getType() == EntityType.WOLF) {
            int dogeSpawnChance = plugin.economy.getInt("dogeSpawnChance");
            int random = (int) (Math.random() * 100 + 1);
            if (random <= dogeSpawnChance) {
                setupDoge((LivingEntity) e.getEntity());
            }
        }
    }

    @EventHandler
    public void onDogeRightClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getPersistentDataContainer().has(dogeKey, PersistentDataType.STRING)) {
            plugin.getLogger().info("doge right clicked");
        }
    }

    @EventHandler
    public void onDogeKilled(EntityDeathEvent e) {
        if (e.getEntity().getPersistentDataContainer().has(dogeKey, PersistentDataType.STRING)) {
            plugin.getLogger().info("doge killed");
        }
    }

    public void setupDoge(LivingEntity doge) {
        doge.setCustomName("doge");
        doge.setCustomNameVisible(true);
        doge.getPersistentDataContainer().set(dogeKey, PersistentDataType.STRING, "doge");

        plugin.getLogger().info("doge spawned");
    }
}
