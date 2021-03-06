package com.daki.main.commands;

import com.daki.main.Cache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllFireworks_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;

        if (!Cache.getAllFireworksPanels().get(player).getInventoryPages().isEmpty()) {
            player.openInventory(Cache.getAllFireworksPanels().get(player).getInventoryPages().get(0));
        } else {
            player.sendMessage(ChatColor.RED + "There's no saved fireworks available.");
        }

        return true;

    }

}
