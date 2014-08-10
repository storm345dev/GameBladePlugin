package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.bans.BanHandler.Time;
import org.stormdev.gbapi.bans.PunishmentLogs.PunishmentType;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.bans.BanPanel;
import org.stormdev.gbplugin.bans.BanTime;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class PunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED+"Players only!");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if(args.length > 0 && cmd.getName().equalsIgnoreCase("ban")){
			if(args.length < 3){
				return false;
			}
			final String name = args[0];
			String timeRaw = args[1];
			
			final StringBuilder reason = new StringBuilder(args[2]);
			for(int i=3;i<args.length;i++){
				reason.append(" ").append(args[i]);
			}
			
			final Time time = BanTime.getFromUserInput(timeRaw);
			if(time == null){
				sender.sendMessage(ChatColor.RED+"Invalid time format!");
				sender.sendMessage(ChatColor.RED+"Valid formats:");
				sender.sendMessage(ChatColor.GREEN+"'forever' = forever");
				sender.sendMessage(ChatColor.GREEN+"'1s' = 1 second");
				sender.sendMessage(ChatColor.GREEN+"'1' = 1 minute");
				sender.sendMessage(ChatColor.GREEN+"'1h' = 1 hour");
				sender.sendMessage(ChatColor.GREEN+"'1d' = 1 day");
				sender.sendMessage(ChatColor.GREEN+"'1w' = 1 week");
				sender.sendMessage(ChatColor.GREEN+"'1m' = 1 month");
				sender.sendMessage(ChatColor.GREEN+"'1y' = 1 year");
				return true;
			}
			
			Player toBan = Bukkit.getPlayerExact(name);
			if(toBan != null){
				GameBlade.api.getBans().ban(toBan, player, reason.toString(), time);
			}
			else {
				Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

					@Override
					public void run() {
						GameBlade.api.getBans().ban(PlayerIDFinder.getMojangID(name).getID(), player, reason.toString(), time);
						return;
					}});
			}
			
			String duration = time.isForever() ? "forever":((int) (time.getDuration()/1000/60/60))+" hours";
			player.sendMessage(ChatColor.GREEN+"Banned!");
			
			for(Player p:Bukkit.getOnlinePlayers()){
				if(p.hasPermission("gameblade.admin")){
					p.sendMessage(ChatColor.YELLOW+player.getName()+" has banned "+name+" for "+reason+" for "+duration+"!");
				}
			}
			GameBlade.logger.info(ChatColor.YELLOW+player.getName()+" has banned "+name+" for "+reason+" for "+duration+"!");
			return true;
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
