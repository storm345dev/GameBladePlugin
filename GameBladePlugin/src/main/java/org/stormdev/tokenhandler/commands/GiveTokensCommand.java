package org.stormdev.tokenhandler.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class GiveTokensCommand implements CommandExecutor {

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
		
		sender.sendMessage(ChatColor.GRAY+"Awarding tokens...");
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				GameBlade.api.getTokenHandler().awardTokens(name, tokens);
				return;
			}});
		
		return true;
	}

}
