package org.stormdev.gbplugin.bans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.utils.MetaValue;

public class ChatInput {
	public static final String meta = "gameblade.chatinput";
	private String name;
	private InputValidator iv;
	
	public ChatInput(Player player, InputValidator iv){
		this.iv = iv;
		this.name = player.getName();
		player.setMetadata(meta, new MetaValue(this, GameBlade.plugin));
		player.sendMessage(iv.getHelpMessage());
	}
	
	public void onChat(Player player, String in){
		if(!iv.isValid(player, in)){
			player.sendMessage(iv.getInvalidMessage());
			destroy();
			return;
		}
		iv.onValidInput(player, in);
		destroy();
	}
	
	public void destroy(){
		Player player = Bukkit.getPlayer(name);
		if(player != null){
			player.removeMetadata(meta, GameBlade.plugin);
		}
	}
	
	public static interface InputValidator {
		
		public String getHelpMessage();
		public boolean isValid(Player player, String input);
		public void onValidInput(Player player, String input);
		public String getInvalidMessage();
	}
}
