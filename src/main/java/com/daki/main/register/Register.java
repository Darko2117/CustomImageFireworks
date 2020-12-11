package com.daki.main.register;

import com.daki.main.CIF;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Register extends JavaPlugin {

    public static void registerEvents() {

        registerEvents(

        );

    }

    private static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            CIF.getInstance().getServer().getPluginManager().registerEvents(listener, CIF.getInstance());
        }
    }

    public static void registerCommands() {

    }

}
