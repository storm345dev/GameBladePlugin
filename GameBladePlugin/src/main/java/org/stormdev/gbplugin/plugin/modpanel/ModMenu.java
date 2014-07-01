package org.stormdev.gbplugin.plugin.modpanel;

import im.mta.coremanager.modmenu.MainMenu;
import im.mta.coremanager.modmenu.ServerSettingsListener;
import im.mta.coremanager.modmenu.YourSettingsListener;

import org.bukkit.entity.Player;

public class ModMenu {
	public static void open(Player player){
		MainMenu.open(player);
	}
	
	public static void init(){
		new ServerSettingsListener();
		new YourSettingsListener();
	}
}
