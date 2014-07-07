package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.bans.BanPanel;

public class PunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(player.hasMetadata(BanPanel.meta)){
			return true;
		}
		
		new BanPanel(player);
		return true;
	}

}
