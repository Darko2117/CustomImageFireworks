package com.daki.main.UI.panels;

import com.daki.main.UI.Icons;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PanelManager implements Listener {

    public static HashMap<Player, StartingPanel> startingPanels = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin_UILoader(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        StartingPanel startingPanel = new StartingPanel(player);
        startingPanels.put(player, startingPanel);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit_UIUnloader(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        startingPanels.remove(player);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick_UIClickListener(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (startingPanels.get(player).getInventory().equals(inventory)) {

            if (clickedItem.equals(Icons.getBlankSpaceIcon())) {
                event.setCancelled(true);
                return;
            }
            if (clickedItem.equals(Icons.getCreateNewFireworkIcon())) {
                event.setCancelled(true);
                player.closeInventory();
                player.chat("/cifcreatefireworks");
                return;
            }
            if (clickedItem.equals(Icons.getConfigReloadIcon())) {
                event.setCancelled(true);
                player.closeInventory();
                player.chat("/cifreload");
                return;
            }
        }

    }

}
