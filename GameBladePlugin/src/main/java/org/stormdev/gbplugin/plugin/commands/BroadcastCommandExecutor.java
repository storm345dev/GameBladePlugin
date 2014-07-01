package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.modpanel.ModMenu;

public class BroadcastCommandExecutor implements CommandExecutor {
private GameBlade plugin;
	
	public BroadcastCommandExecutor(GameBlade plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("broadcast")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED+"Players only!");
				return true;
			}
			if(args.length < 1){
				return false;
			}
			if (args.length > 0) {
                String message = "";
                for (String part : args) {
                    if (message != "") message = message + " ";
                    message = message + part;
                }
                Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.BLACK + " » " + ChatColor.AQUA + "" + ChatColor.translateAlternateColorCodes('&', message));
            }
			return true;
		}
		return false;
	}
}
