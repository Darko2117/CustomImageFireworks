package com.daki.main.commands;

import com.daki.main.CIF;
import com.daki.main.UI.panels.PanelManager;
import com.daki.main.config.Config;
import com.daki.main.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Reload_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        new BukkitRunnable() {
            @Override
            public void run() {

                CIF.getInstance().reloadConfig();
                Config.checkAndSaveDefault();
                Config.reloadCachedConfigData();
                Database.reinitialize(false);
                PanelManager.reloadPanelsForAllPlayers();

                commandSender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
                CIF.getInstance().getLogger().info(ChatColor.GREEN + "Plugin reloaded!");

            }
        }.runTaskAsynchronously(CIF.getInstance());

        return true;

    }

}
