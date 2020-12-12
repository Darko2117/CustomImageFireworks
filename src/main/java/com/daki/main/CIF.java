package com.daki.main;

import com.daki.main.config.Config;
import com.daki.main.database.Database;
import com.daki.main.register.Register;
import org.bukkit.plugin.java.JavaPlugin;

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

    }

}