package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.modpanel.ModMenu;

public class ModCommandExecutor implements CommandExecutor {
	private GameBlade plugin;
	
	public ModCommandExecutor(GameBlade plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("mod")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED+"Players only!");
				return true;
			}
			//TODO Open mod panel
			ModMenu.open((Player) sender);
			return true;
		}
		return false;
	}
}
