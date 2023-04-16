package dev.wonkypigs.minememer;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolders implements InventoryHolder {
    Inventory inv;
    String s;
    public GUIHolders(String type) {
        this.s = type;
    }
    @Override
    public Inventory getInventory() {
        return inv;
    }
    public String getType() {
        return s;
    }
    public void setType(String type) { this.s = type; }
}
