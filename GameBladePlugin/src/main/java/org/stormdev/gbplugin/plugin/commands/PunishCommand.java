package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.bans.PunishmentLogs.PunishmentType;
import org.stormdev.gbplugin.bans.BanPanel;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class PunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length > 0 && cmd.getName().equalsIgnoreCase("ban")){
			player.sendMessage(ChatColor.RED+"I know you tried to do this with a command; but bans MUST use the mod panel (Reasons and times, etc)");
		}
		else if(args.length > 0){
			//They're tryng to do the commands
			if(args.length < 2){
				return false;
			}
			String name = args[0];
			StringBuilder reason = new StringBuilder(args[1]);
			for(int i=2;i<args.length;i++){
				reason.append(" ").append(args[i]);
			}
			
			if(cmd.getName().equalsIgnoreCase("kick")){
				player.sendMessage(ChatColor.GRAY+"Reason: "+reason);
				
				Player toKick = Bukkit.getPlayerExact(name);
				
				if(toKick == null){
					player.sendMessage(ChatColor.GRAY+"They are not online!");
					return true;
				}
				
				player.sendMessage(ChatColor.GREEN+"Kicked!");
				for(Player p:Bukkit.getOnlinePlayers()){
					if(p.hasPermission("gameblade.admin")){
						p.sendMessage(ChatColor.YELLOW+player.getName()+" has kicked "+name+" for "+reason+"!");
					}
				}
				toKick.kickPlayer(reason.toString());
				GameBlade.api.getPunishmentLogger().log(toKick, PunishmentType.KICK, reason.toString(), player.getName());
			}
			else if(cmd.getName().equalsIgnoreCase("warn")){
				Player toWarn = Bukkit.getPlayerExact(name);
				
				if(toWarn == null){
					player.sendMessage(ChatColor.GRAY+"They are not online!");
					return true;
				}
				
				player.sendMessage(ChatColor.GREEN+"Warned!");
				for(Player p:Bukkit.getOnlinePlayers()){
					if(p.hasPermission("gameblade.admin")){
						p.sendMessage(ChatColor.YELLOW+player.getName()+" has warned "+name+" for "+reason+"!");
					}
				}
				
				toWarn.sendMessage(ChatColor.RED+"Warned by "+ChatColor.GOLD+player.getName()+ChatColor.RED+" for: "+ChatColor.WHITE+reason);
				GameBlade.api.getPunishmentLogger().log(toWarn, PunishmentType.WARN, reason.toString(), player.getName());
			}
			return true;
		}
		
		if(player.hasMetadata(BanPanel.meta)){
			return true;
		}
		
		new BanPanel(player);
		return true;
	}

}
