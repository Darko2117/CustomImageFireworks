package com.daki.main.UI.panels;

import com.daki.main.UI.Icons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class StartingPanel {

    String inventoryTitle = ChatColor.translateAlternateColorCodes('&', "&c&lC&6&lu&e&ls&a&lt&9&lo&d&lm &c&lI&6&lm&e&la&a&lg&9&le &d&lF&c&li&6&lr&e&le&a&lw&9&lo&d&lr&c&lk&6&ls&9&l!");
    Integer inventorySize = 9;
    Inventory inventory = Bukkit.createInventory(null, inventorySize, inventoryTitle);

    public StartingPanel(Player player) {

        if (player.hasPermission("cif.list-own-fireworks")) {
            inventory.setItem(0, Icons.getPlayersAvailableFireworksIcon());
        }
        if (player.hasPermission("cif.list-all-fireworks")) {
            inventory.setItem(6, Icons.getAllFireworksIcon());
        }
        if (player.hasPermission("cif.create-fireworks")) {
            inventory.setItem(7, Icons.getCreateNewFireworkIcon());
        }
        if (player.hasPermission("cif.reload")) {
            inventory.setItem(8, Icons.getConfigReloadIcon());
        }

        for (Integer i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, Icons.getBlankSpaceIcon());
            }
        }

    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }

    public void setInventoryTitle(String inventoryTitle) {
        this.inventoryTitle = inventoryTitle;
    }

    public Integer getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(Integer inventorySize) {
        this.inventorySize = inventorySize;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

}
