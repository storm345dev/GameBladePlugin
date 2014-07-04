package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.Config;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class HatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(!Config.enableHats.getValue()){
			sender.sendMessage(ChatColor.RED+"Hats are disabled while on this server. Go to another, such as a lobby, to use hats.");
			return true;
		}
		GameBlade.plugin.cosmeticManager.getHatMenu().open(player);
		return true;
	}

}
