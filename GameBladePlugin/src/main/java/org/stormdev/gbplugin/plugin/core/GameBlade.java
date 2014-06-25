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
		
		logger.info("GameBladePlugin enabled!");
	}
	
	@Override
	public void onDisable(){
		
		logger.info("GameBladePlugin disabled!");
	}
}
