package org.stormdev.gbplugin.plugin.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class GameBlade extends JavaPlugin {
	public static CustomLogger logger;
	public static GameBlade plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		logger = new CustomLogger(Bukkit.getConsoleSender(), getLogger());
		
		if(!initAPI()){
			//TODO Error
		}
		
		logger.info("GameBladePlugin enabled!");
	}
	
	@Override
	public void onDisable(){
		logger.info("GameBladePlugin disabled!");
	}
	
	private boolean initAPI(){
		try {
			Class.forName("org.stormdev.gbapi.core.APIProvider");
		} catch (ClassNotFoundException e) {
			logger.info("Bad GameBladePlugin file, doesn't include the API! :( Please try a different plugin file!");
			return false;
		}
		
		//TODO
		
		return true;
	}
}
