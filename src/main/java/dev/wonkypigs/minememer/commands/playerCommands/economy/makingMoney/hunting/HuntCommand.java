package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.hunting;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.getPlayerItemAmount;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.*;

@CommandAlias("mm|minememer")
public class HuntCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();
    NamespacedKey animalItemKey = new NamespacedKey(plugin, plugin.animalItemKeyName);

    @Subcommand("hunt|hunting")
    public void playerHunt(Player player) {
        if (getPlayerItemAmount(player, "hunting_rifle") == 0) {
            player.sendMessage(plugin.lang.getString("need-item")
                    .replace("&", "§")
                    .replace("{item}", plugin.items.getString("items.hunting_rifle.menu_name").replace("&", "§"))
            );
            return;
        }
        huntMenu(player);
    }

    public void huntMenu(Player player) {
        Inventory inv = plugin.getServer().createInventory(new GUIHolders("hunting"), 45, plugin.lang.getString("hunting-menu-title")
                .replace("&", "§")
        );
        // menu background
        setMenuBackground(inv, plugin.menubg2, 0, 9, " ");
        setMenuBackground(inv, Material.GRASS, 9, 36,
                plugin.lang.getString("hunting-menu-grass-name")
                        .replace("&", "§")
        );
        setMenuBackground(inv, plugin.menubg2, 36, 45, " ");

        // top fishing rod item
        ItemStack rifle = new ItemStack(Material.BOW);
        ItemMeta rifleMeta = rifle.getItemMeta();
        rifleMeta.setDisplayName(plugin.lang.getString("hunting-rifle-menu-item-name")
                .replace("&", "§")
        );
        rifle.setItemMeta(rifleMeta);

        inv.setItem(4, rifle);
        startHuntingGame(player, inv, 9, 35);
    }

    public void startHuntingGame(Player player, Inventory inv, int minslot, int maxslot) {
        player.openInventory(inv);
        Random huntRandom = new Random();
        List<Integer> freeSlots = IntStream.range(minslot, maxslot+1).boxed().collect(Collectors.toList());

        int animalAmtLow = plugin.economy.getIntegerList("hunt-animal-amount-range").get(0);
        int animalAmtHigh = plugin.economy.getIntegerList("hunt-animal-amount-range").get(1);
        int animalAmt = huntRandom.ints(animalAmtLow, animalAmtHigh).findFirst().getAsInt();
        for (int i = 0; i < animalAmt+1; i++) {
            ItemStack animal = getAnimalItem();
            int animalSlot = huntRandom.ints(minslot, maxslot+1).findFirst().getAsInt();
            while (!freeSlots.contains(animalSlot)) {
                animalSlot = huntRandom.ints(minslot, maxslot+1).findFirst().getAsInt();
            }
            inv.setItem(animalSlot, animal);
        }
    }

    public ItemStack getAnimalItem() {
        Random animalRandom = new Random();
        List<String> animalNames = plugin.economy.getStringList("hunt-animal-names");
        List<String> animalSkins = plugin.economy.getStringList("hunt-animal-skins");
        String animalName = animalNames.get(animalRandom.ints(0, animalNames.size()).findFirst().getAsInt());
        String animalSkin = animalSkins.get(animalNames.indexOf(animalName));

        ItemStack animal = getCustomHead(animalSkin);
        SkullMeta animalMeta = (SkullMeta) animal.getItemMeta();
        animalMeta.setDisplayName(plugin.lang.getString("hunting-animal-display-name")
                .replace("&", "§")
                .replace("{animal}", animalName)
        );

        animalMeta.getPersistentDataContainer().set(animalItemKey, PersistentDataType.STRING, animalName);

        animal.setItemMeta(animalMeta);
        return animal;
    }
}
