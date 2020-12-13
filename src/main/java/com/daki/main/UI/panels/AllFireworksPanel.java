package com.daki.main.UI.panels;

import com.daki.main.Cache;
import com.daki.main.UI.Icons;
import com.daki.main.firework.Firework;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllFireworksPanel {

    String inventoryTitle = ChatColor.translateAlternateColorCodes('&', "&9All fireworks!");
    Integer inventorySize = 54;
    List<Inventory> inventoryPages = new ArrayList<>();

    AllFireworksPanel(){

        List<ItemStack> fireworkIcons = new ArrayList<>();

        for (Firework firework : Cache.getLoadedFireworks()) {

            ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(firework.getName());
            meta.setLore(Collections.singletonList("ID: " + firework.getID()));
            item.setItemMeta(meta);
            fireworkIcons.add(item);

        }

        while (!fireworkIcons.isEmpty()) {

            Inventory page = Bukkit.createInventory(null, inventorySize, inventoryTitle);

            for (Integer i = 0; i < 45; i++) {

                if (fireworkIcons.isEmpty()) {
                    break;
                }

                page.setItem(i, fireworkIcons.get(0));
                fireworkIcons.remove(0);

            }

            if (!inventoryPages.isEmpty()) {
                page.setItem(45, Icons.getPreviousPageItemStack());
            }
            if (!fireworkIcons.isEmpty()) {
                page.setItem(53, Icons.getNextPageItemStack());
            }

            inventoryPages.add(page);

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

    public List<Inventory> getInventoryPages() {
        return inventoryPages;
    }

    public void setInventoryPages(List<Inventory> inventoryPages) {
        this.inventoryPages = inventoryPages;
    }

}
