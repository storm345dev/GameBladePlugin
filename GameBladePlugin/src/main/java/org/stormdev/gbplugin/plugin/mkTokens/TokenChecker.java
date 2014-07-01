package org.stormdev.gbplugin.plugin.mkTokens;

import org.bukkit.Bukkit;

public class TokenChecker {
	public static void handleMarioKart(){
		if(Bukkit.getPluginManager().getPlugin("MarioKart") == null){
			return;
		}
		try {
			Class.forName("net.stormdev.mario.server.EconProvider");
			try {
				if(Bukkit.getPluginManager().getPlugin("MarioKart") != null){
					net.stormdev.mario.mariokart.MarioKart.overrideEcon(new org.stormdev.gbplugin.plugin.mkTokens.TokenEcon()); //Make MarioKart use tokens
				}
			} catch (NoClassDefFoundError e) {
				//Who cares? There just isn't MarioKart...
			}
		} catch (ClassNotFoundException e1) {
			//No MarioKart...
		}
	}
}
