package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.autodeploy.UpdateDeployer;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class UpdateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if((sender instanceof Player) && !((Player)sender).isOp()){
			sender.sendMessage("No access to this command");
			return true;
		}
		if(args.length < 2){
			sender.sendMessage("Invalid usage:");
			return false;
		}
		
		final String plugName = args[0];
		final String URL = args[1];
		
		sender.sendMessage("Updating "+plugName+" from "+URL+"...");
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				UpdateDeployer.update(plugName, URL);
				return;
			}});
		return true;
	}

}
