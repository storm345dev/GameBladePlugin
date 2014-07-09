package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CreateVillagerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(args.length < 1){
			return false;
		}
		
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		
		Player player = (Player) sender;
		Location loc = player.getLocation();
		
		StringBuilder sb = new StringBuilder(args[0]);
		for(int i=1;i<args.length;i++){
			sb.append(" ").append(args[i]);
		}
		
		String name = sb.toString();
		
		if(name.equalsIgnoreCase("cosmetics")){
			name = "Cosmetics";
		}
		
		GameBlade.api.getMenuVillagerManager().spawnVillager(name, loc);
		sender.sendMessage(ChatColor.GREEN+"Successfully Spawned! (Make sure they cannot escape the desired area)");
		return true;
	}

}
