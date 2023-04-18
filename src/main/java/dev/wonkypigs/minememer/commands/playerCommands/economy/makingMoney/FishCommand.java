package dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;
import static dev.wonkypigs.minememer.helpers.MenuHelpers.*;

@CommandAlias("mm|minememer")
public class FishCommand extends BaseCommand {
    private static final MineMemer plugin = MineMemer.getInstance();

    @Subcommand("fishing|fish")
    public void fishForFish(Player player) {
        if (getPlayerItemAmount(player, "fishing_rod") == 0) {
            player.sendMessage(plugin.lang.getString("no-fishing-rod")
                    .replace("&", "§")
            );
            return;
        }
        fishMenu(player);
    }

    public void fishMenu(Player player) {
        Inventory inv = plugin.getServer().createInventory(new GUIHolders("fishing"), 45, plugin.lang.getString("fishing-menu-title")
                .replace("&", "§")
        );
        // menu background
        setMenuBackground(inv, plugin.menubg2, 0, 9, " ");
        setMenuBackground(inv, Material.BLUE_STAINED_GLASS_PANE, 9, 36,
                plugin.lang.getString("fishing-menu-water-name")
                        .replace("&", "§")
        );
        setMenuBackground(inv, plugin.menubg2, 36, 45, " ");

        // top fishing rod item
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta rodMeta = rod.getItemMeta();
        rodMeta.setDisplayName(plugin.lang.getString("fishing-rod-menu-item-name")
                .replace("&", "§")
        );
        rod.setItemMeta(rodMeta);

        inv.setItem(4, rod);
        startFishingGame(player, inv, 9, 35);
    }

    public void startFishingGame(Player player, Inventory inv, int minslot, int maxslot) {
        player.openInventory(inv);
        ItemStack fish = getFishItem();
        Random fishRandom = new Random();
        List<Integer> freeSlots = IntStream.range(minslot, maxslot+1).boxed().collect(Collectors.toList());

        int fishAmtLow = plugin.economy.getIntegerList("fish-amount-range").get(0);
        int fishAmtHigh = plugin.economy.getIntegerList("fish-amount-range").get(1);
        int fishAmt = fishRandom.ints(fishAmtLow, fishAmtHigh).findFirst().getAsInt();
        for (int i = 0; i < fishAmt+1; i++) {
            int fishSlot = fishRandom.ints(minslot, maxslot+1).findFirst().getAsInt();
            while (!freeSlots.contains(fishSlot)) {
                fishSlot = fishRandom.ints(minslot, maxslot+1).findFirst().getAsInt();
            }
            inv.setItem(fishSlot, fish);
        }
    }

    public ItemStack getFishItem() {
        ItemStack fish = new ItemStack(Material.TROPICAL_FISH);
        ItemMeta fishMeta = fish.getItemMeta();
        fishMeta.setDisplayName(plugin.lang.getString("fish-display-name")
                .replace("&", "§")
        );

        fish.setItemMeta(fishMeta);
        return fish;
    }
}
