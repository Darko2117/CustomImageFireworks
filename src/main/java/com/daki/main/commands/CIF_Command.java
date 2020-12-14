package com.daki.main.commands;

import com.daki.main.Cache;
import com.daki.main.UI.panels.StartingPanel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CIF_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;

        Cache.removeStartingPanel(player);
        Cache.addStartingPanel(player, new StartingPanel(player));
        player.openInventory(Cache.getStartingPanels().get(player).getInventory());

        return true;

    }

}
