package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class UnbanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(args.length < 1){
			return false;
		}
		final String name = args[0];
		sender.sendMessage(ChatColor.GREEN+"Unbanning player...");
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				String uuid;
				try {
					uuid = PlayerIDFinder.getMojangID(name).getID();
				} catch (Exception e) {
					//Player doesnt exist
					return;
				}
				if(uuid == null){
					return;
				}
				GameBlade.api.getBans().unban(uuid);
				return;
			}});
		
		return true;
	}

}
