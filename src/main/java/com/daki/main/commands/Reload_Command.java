package com.daki.main.commands;

import com.daki.main.CIF;
import com.daki.main.Cache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        CIF.getInstance().reloadConfig();
        Cache.reloadCachedDatabaseData();
        Cache.reloadCachedDatabaseData();

        commandSender.sendMessage("Yes");

        return true;

    }

}
