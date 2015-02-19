package org.stormdev.gbplugin.plugin.core;

import net.stormdev.MTA.SMPlugin.core.Core;
import net.stormdev.MTA.SMPlugin.core.ServerMonitor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.utils.Colors;

public class AutoRestart {
	private static boolean restartingInProgress = false;
	private static long restartStartTime = 0l;
	private static boolean restartAlerted = false;
	private static String PREFIX = ChatColor.BLUE+"Game"+ChatColor.GOLD+"Blade"+ChatColor.RESET+""+ChatColor.GRAY+" | "+ChatColor.RESET;
	
	public static void start(){
		if(Config.autoRestartEnabled.getValue()){
			Core.logger.info("Auto-Restart is enabled! The server will restart when the TPS drops below "+Config.autoRestartTPSThreshold.getValue()+"!");
		}
		
		Bukkit.getScheduler().runTaskTimer(Core.plugin, new Runnable(){

			@Override
			public void run() {
				double tps = ServerMonitor.getTPS();
				if(restartAlerted){
					return; //Just restart no matter what
				}
				if(restartingInProgress){
					if(tps > Config.autoRestartTPSThreshold.getValue() && Config.autoRestartEnabled.getValue()){ //TPS has recovered
						restartingInProgress = false;
						Core.logger.info("Auto-Restart detected TPS is back above theshold and has subsequently cancelled restarting the server!");
						return;
					}
					//It's still bad
					long neededTimeMillis = (long) (Config.autoRestartTPSTime.getValue() * 1000l);
					long elapsed = System.currentTimeMillis() - restartStartTime;
					if(elapsed > neededTimeMillis){ //TPS has been bad for too long
						restart();
					}
					return;
				}
				
				if(tps <= Config.autoRestartTPSThreshold.getValue() && Config.autoRestartEnabled.getValue()){
					restartStartTime = System.currentTimeMillis();
					restartingInProgress = true;
					Core.logger.info("Auto-Restart detected TPS below threshold! Server will restart soon if the TPS doesn't soon recover!");
				}
				return;
			}}, 100l, 200l); //Every 10 seconds
	}
	
	private static void restart(){
		restartAlerted = true; //Restart no matter what
		//Alert the server of the restart
		String alertText = PREFIX+Colors.colorise(Config.autoRestartAlert.getValue());
		Bukkit.broadcastMessage(alertText);
		
		//COUNTDOWN
		Bukkit.getScheduler().runTaskAsynchronously(Core.plugin, new Runnable(){

			@Override
			public void run() {
				int alertTime = (int) Math.round(Config.autoRestartAlertTime.getValue());
				if(alertTime <= 0){
					alertTime = 1;
				}
				
				for(int timeLeft=alertTime;timeLeft>=0;timeLeft--){
					if(timeLeft == 0){
						Bukkit.broadcastMessage(PREFIX+"Server restarting now!");
					}
					else if(timeLeft <= 10){
						Bukkit.broadcastMessage(PREFIX+"Server restarting in "+timeLeft+" seconds!");
					}
					else if(timeLeft % 10 == 0){
						Bukkit.broadcastMessage(PREFIX+"Server restarting in "+timeLeft+" seconds!");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						//MEH
					}
				}
				return;
			}});
		
		long alertTime = (long) (Config.autoRestartAlertTime.getValue()*20l);
		if(alertTime <= 0){
			alertTime = 1;
		}
		
		Bukkit.getScheduler().runTaskLater(Core.plugin, new Runnable(){

			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.RED+"Server restarting!");
				
				// Restart the server
				String serverName = Config.autoRestartServer.getValue();
				for(Player player:Bukkit.getOnlinePlayers()){
					PlayerServerSender.sendToServer(player, serverName);
				}
				
				Bukkit.getScheduler().runTaskLater(Core.plugin, new Runnable(){ //Delayed by 3s to allow everybody to disconnect

					@Override
					public void run() {
						for(Player player:Bukkit.getOnlinePlayers()){
							player.kickPlayer("Server restarting!"); //Maybe not on bungee?
						}
						
						//Now to restart the server!
						if(!Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Config.autoRestartCommand.getValue())){
							Bukkit.getServer().shutdown();
						}
						return;
					}}, 60l);
				return;
			}}, alertTime+20l);
	}
	
	public static void forceRestart(){
		restart();
	}
}
