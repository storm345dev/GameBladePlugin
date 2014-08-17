package org.stormdev.gbplugin.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.ranks.Rank;
import org.stormdev.gbplugin.plugin.ranks.RankSetting;

public class SetRankCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias,
			String[] args) {
		if(args.length < 2){
			return false;
		}
		
		final String player = args[0];
		String rankRaw = args[1];
		Rank rank = null;
		
		final boolean name = player.length() <= 16;
		
		try {
			int rankId = Integer.parseInt(rankRaw);
			rank = Rank.getRank(rankId);
		} catch (NumberFormatException e) {
			rank = Rank.valueOf(rankRaw);
		}
		
		if(rank == null){
			sender.sendMessage("FAILED TO SET RANK "+rankRaw+" IS NOT A VALID RANK");
			return true;
		}
		sender.sendMessage(ChatColor.GREEN+"Setting "+player+"'s rank to "+rank.name());
		
		final Rank rnk = rank;
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				if(name){
					RankSetting.setRankByName(player, rnk);
				}
				else {
					RankSetting.setRankByUUID(player, rnk);
				}
				return;
			}});
		
		return true;
	}

}
