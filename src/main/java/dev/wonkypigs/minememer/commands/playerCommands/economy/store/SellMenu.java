package dev.wonkypigs.minememer.commands.playerCommands.economy.store;

import dev.wonkypigs.minememer.GUIHolders;
import dev.wonkypigs.minememer.MineMemer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

import static dev.wonkypigs.minememer.helpers.MenuHelpers.setMenuBackground;

public class SellMenu {
    private static final MineMemer plugin = MineMemer.getInstance();

    public static void openSellMenu(Player player, Inventory inv, InventoryHolder holder) {
        // change holder type
        ((GUIHolders) holder).setType("store_sell");

        // menu background

    }
}
