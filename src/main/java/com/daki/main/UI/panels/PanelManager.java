package com.daki.main.UI.panels;

import com.daki.main.Cache;
import com.daki.main.UI.Icons;
import com.daki.main.firework.Firework;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class PanelManager implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin_UILoader(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        StartingPanel startingPanel = new StartingPanel(player);
        Cache.addStartingPanel(player, startingPanel);

        ChooseImageNamePanel chooseImageNamePanel = new ChooseImageNamePanel();
        Cache.addChooseImageNamePanel(player, chooseImageNamePanel);

        AllFireworksPanel allFireworksPanel = new AllFireworksPanel();
        Cache.addAllFireworksPanel(player, allFireworksPanel);

        Cache.addWhatPageOfChooseImageNamePanelIsPlayerOn(player, 0);
        Cache.addWhatPageOfAllFireworksPanelIsPlayerOn(player, 0);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit_UIUnloader(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Cache.removeStartingPanel(player);
        Cache.removeChooseImageNamePanel(player);
        Cache.removeAllFireworksPanel(player);

        Cache.removeWhatPageOfChooseImageNamePanelIsPlayerOn(player);
        Cache.removeWhatPageOfAllFireworksPanelIsPlayerOn(player);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick_UIClickListener(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getCurrentItem() == null) return;

        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        //StartingPanel clicks

        if (Cache.getStartingPanels().get(player).getInventory().equals(inventory)) {

            event.setCancelled(true);

            if (clickedItem.equals(Icons.getBlankSpaceIcon())) {
                return;
            } else if (clickedItem.equals(Icons.getAllFireworksIcon())) {
                player.closeInventory();
                player.chat("/ciflistallfireworks");
                return;
            } else if (clickedItem.equals(Icons.getCreateNewFireworkIcon())) {
                player.closeInventory();
                player.chat("/cifcreatefireworks");
                return;
            } else if (clickedItem.equals(Icons.getConfigReloadIcon())) {
                player.closeInventory();
                player.chat("/cifreload");
                return;
            }

        }

        //ChooseImageNamePanel clicks

        if (Cache.getChooseImageNamePanels().get(player).getInventoryPages().contains(inventory)) {

            event.setCancelled(true);

            if (!player.isConversing()) return;

            if (clickedItem.equals(Icons.getPreviousPageItemStack())) {
                Cache.addWhatPageOfChooseImageNamePanelIsPlayerOn(player, Cache.getWhatPageOfChooseImageNamePanelIsPlayerOn().get(player) - 1);
                player.closeInventory();
                player.openInventory(Cache.getChooseImageNamePanels().get(player).getInventoryPages().get(Cache.getWhatPageOfChooseImageNamePanelIsPlayerOn().get(player)));
                return;
            } else if (clickedItem.equals(Icons.getNextPageItemStack())) {
                Cache.addWhatPageOfChooseImageNamePanelIsPlayerOn(player, Cache.getWhatPageOfChooseImageNamePanelIsPlayerOn().get(player) + 1);
                player.closeInventory();
                player.openInventory(Cache.getChooseImageNamePanels().get(player).getInventoryPages().get(Cache.getWhatPageOfChooseImageNamePanelIsPlayerOn().get(player)));
                return;
            } else {
                player.acceptConversationInput(clickedItem.getItemMeta().getDisplayName());
                player.closeInventory();
                return;
            }

        }

        //AllFireworksPanel clicks

        if (Cache.getAllFireworksPanels().get(player).getInventoryPages().contains(inventory)) {

            event.setCancelled(true);

            if (clickedItem.equals(Icons.getPreviousPageItemStack())) {
                Cache.addWhatPageOfAllFireworksPanelIsPlayerOn(player, Cache.getWhatPageOfAllFireworksPanelIsPlayerOn().get(player) - 1);
                player.closeInventory();
                player.openInventory(Cache.getAllFireworksPanels().get(player).getInventoryPages().get(Cache.getWhatPageOfAllFireworksPanelIsPlayerOn().get(player)));
                return;
            } else if (clickedItem.equals(Icons.getNextPageItemStack())) {
                Cache.addWhatPageOfAllFireworksPanelIsPlayerOn(player, Cache.getWhatPageOfAllFireworksPanelIsPlayerOn().get(player) + 1);
                player.closeInventory();
                player.openInventory(Cache.getAllFireworksPanels().get(player).getInventoryPages().get(Cache.getWhatPageOfAllFireworksPanelIsPlayerOn().get(player)));
                return;
            } else {
                player.closeInventory();
                String ID = clickedItem.getItemMeta().getLore().get(0).substring(4);
                Firework firework = Cache.getFireworksByID(ID);

                org.bukkit.entity.Firework fireworkEntity = (org.bukkit.entity.Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fireworkMeta = fireworkEntity.getFireworkMeta();
                fireworkMeta.setPower(Integer.parseInt(firework.getPower()));
                return;
            }

        }

    }

    public static void reloadPanelsForAllPlayers() {

        Cache.clearStartingPanels();
        Cache.clearChooseImageNamePanels();
        Cache.clearAllFireworksPanels();

        Cache.clearWhatPageOfChooseImageNamePanelIsPlayerOn();
        Cache.clearWhatPageOfAllFireworksPanelIsPlayerOn();

        for (Player player : Bukkit.getOnlinePlayers()) {

            StartingPanel startingPanel = new StartingPanel(player);
            Cache.addStartingPanel(player, startingPanel);
            ChooseImageNamePanel chooseImageNamePanel = new ChooseImageNamePanel();
            Cache.addChooseImageNamePanel(player, chooseImageNamePanel);
            AllFireworksPanel allFireworksPanel = new AllFireworksPanel();
            Cache.addAllFireworksPanel(player, allFireworksPanel);

            Cache.addWhatPageOfChooseImageNamePanelIsPlayerOn(player, 0);
            Cache.addWhatPageOfAllFireworksPanelIsPlayerOn(player, 0);

        }

    }

}