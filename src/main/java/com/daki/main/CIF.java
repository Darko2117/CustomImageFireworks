package com.daki.main;

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

        Register.registerEvents();

        Register.registerCommands();

        CIF.getInstance().getLogger().info("CustomImageFireworks started...");
        CIF.getInstance().getLogger().info("--------------------------------------------------");

    }

}
