package com.daki.main;

import com.daki.main.UI.Icons;
import com.daki.main.config.Config;
import com.daki.main.database.Database;
import com.daki.main.register.Register;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CIF extends JavaPlugin {

    public static CIF instance;

    public static CIF getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        CIF.getInstance().getLogger().info("--------------------------------------------------");

        Config.checkAndSaveDefault();

        Cache.reloadCachedConfigData();

        Register.registerEvents();
        Register.registerCommands();

        Database.reinitialize();
        Cache.reloadCachedDatabaseData();

        CIF.getInstance().getLogger().info("CustomImageFireworks started...");
        CIF.getInstance().getLogger().info("--------------------------------------------------");

        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){

                    player.getInventory().addItem(Icons.getConfigReloadIcon());
                    player.getInventory().addItem(Icons.getCreateNewFireworkIcon());
                    player.getInventory().addItem(Icons.getPlayersAvailableFireworksIcon());
                    player.getInventory().addItem(Icons.getAllFireworksIcon());
                    player.getInventory().addItem(Icons.getBlankSpaceIcon());

                }
            }
        }.runTaskLater(CIF.getInstance(), 60);

    }

}