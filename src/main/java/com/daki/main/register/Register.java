package com.daki.main.register;

import com.daki.main.CIF;
import com.daki.main.UI.panels.PanelManager;
import com.daki.main.commands.CIF_Command;
import com.daki.main.commands.CreateNewFirework_Command;
import com.daki.main.commands.Reload_Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Register extends JavaPlugin {

    public static void registerEvents() {

        registerEvents(
                new PanelManager()
        );

    }

    private static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            CIF.getInstance().getServer().getPluginManager().registerEvents(listener, CIF.getInstance());
        }
    }

    public static void registerCommands() {

        CIF.getInstance().getCommand("cif").setExecutor(new CIF_Command());
        CIF.getInstance().getCommand("cifreload").setExecutor(new Reload_Command());
        CIF.getInstance().getCommand("cifcreatefireworks").setExecutor(new CreateNewFirework_Command());

    }

}
