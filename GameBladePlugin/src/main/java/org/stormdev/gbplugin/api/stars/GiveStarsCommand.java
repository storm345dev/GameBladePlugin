package org.stormdev.gbplugin.api.stars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class GiveStarsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			if(!(sender.equals(Bukkit.getConsoleSender()))){
				return true; //No command blocks..
			}
		}
		
		int toGive = 0;
		if(args.length < 2){
			return false;
		}
		final String name = args[0];
		try {
			toGive = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			//Invalid number
			return false;
		}
		
		final int tokens = toGive;
		
		double multiplier = 1.0; //TODO
		if(args.length > 2){
			String rank = args[2];
			multiplier = StarMultipliers.getMultiplier(rank);
		}
		
		sender.sendMessage(ChatColor.GRAY+"Awarding stars...");
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				GameBlade.api.getStarsHandler().awardStars(PlayerIDFinder.getMojangID(name).getID(), tokens);
				return;
			}});
		
		return true;
	}

}
