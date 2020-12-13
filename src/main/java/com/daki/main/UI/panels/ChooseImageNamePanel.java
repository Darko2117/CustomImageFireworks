package com.daki.main.UI.panels;

import com.daki.main.Cache;
import com.daki.main.UI.Icons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseImageNamePanel {

    String inventoryTitle = ChatColor.translateAlternateColorCodes('&', "&0Choose a firework image!");
    Integer inventorySize = 54;
    List<Inventory> inventoryPages = new ArrayList<>();

    ChooseImageNamePanel() {

        List<ItemStack> imageNamePapers = new ArrayList<>();

        for (File file : new File(Cache.getImagesPath()).listFiles()) {

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(file.getName());
            item.setItemMeta(meta);
            imageNamePapers.add(item);

        }

        while (!imageNamePapers.isEmpty()) {

            Inventory page = Bukkit.createInventory(null, inventorySize, inventoryTitle);

            for (Integer i = 0; i < 45; i++) {

                if (imageNamePapers.isEmpty()) {
                    break;
                }

                page.setItem(i, imageNamePapers.get(0));
                imageNamePapers.remove(0);

            }

            if (!inventoryPages.isEmpty()) {
                page.setItem(45, Icons.getPreviousPageItemStack());
            }
            if (!imageNamePapers.isEmpty()) {
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
