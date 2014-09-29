package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.storm.misc.CallBack;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CallBackCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			return true;
		}
		
		Player player = (Player) sender;
		
		if(!player.hasMetadata(CallBack.META)){
			player.sendMessage(ChatColor.RED+"You can only use this once!");
			return true;
		}
		
		try {
			CallBack o = (CallBack) player.getMetadata(CallBack.META).get(0).value();
			o.callBack(player);
		} catch (Exception e) {
			player.removeMetadata(CallBack.META, GameBlade.plugin);
			return true;
		}
		return true;
	}

}
