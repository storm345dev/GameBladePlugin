package org.stormdev.gbplugin.api.stars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class MyStarsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		final Player player = (Player) sender;
		
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				try {
					int tokens = Stars.getInstance().getStars(player);
					player.sendMessage(ChatColor.WHITE+"You have: "+ChatColor.RED+tokens+" stars");
				} catch (Exception e) {
					player.sendMessage(ChatColor.RED+"Failed to load your star balance, sorry :(");
				}
				
				return;
			}});
		
		return true;
	}
	
}
