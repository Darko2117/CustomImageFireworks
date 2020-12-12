package com.daki.main.UI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Icons {

    public static ItemStack getConfigReloadIcon() {

        ItemStack icon = new ItemStack(Material.SCUTE);

        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Config reload");

        icon.setItemMeta(meta);

        return icon;

    }

    public static ItemStack getCreateNewFireworkIcon() {

        ItemStack icon = new ItemStack(Material.PAPER);

        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(ChatColor.GRAY + "Create a new firework!");

        icon.setItemMeta(meta);

        return icon;

    }

    public static ItemStack getPlayersAvailableFireworksIcon() {

        ItemStack icon = new ItemStack(Material.FIREWORK_ROCKET);

        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "Your fireworks!");

        icon.setItemMeta(meta);

        return icon;

    }

    public static ItemStack getAllFireworksIcon() {

        ItemStack icon = new ItemStack(Material.FIREWORK_ROCKET);

        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "All fireworks!");

        icon.setItemMeta(meta);

        return icon;

    }

    public static ItemStack getBlankSpaceIcon(){

        ItemStack icon = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GRAY + "");

        icon.setItemMeta(meta);

        return icon;

    }

}