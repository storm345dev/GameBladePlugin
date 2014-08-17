package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvSeeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length < 1){
			return false;
		}
		
		String pName = args[0];
		Player other = Bukkit.getPlayer(pName);
		if(other == null){
			sender.sendMessage(ChatColor.RED+"Sorry, I could not find that player, "+player.getName()+", I tried REEAALLLY hard :( xxx -storm345");
			return true;
		}
		
		player.openInventory(other.getInventory());
		return true;
	}

}
