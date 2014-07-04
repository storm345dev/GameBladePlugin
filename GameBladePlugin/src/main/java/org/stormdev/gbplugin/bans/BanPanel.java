package org.stormdev.gbplugin.bans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.bans.ChatInput.InputValidator;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.utils.MetaValue;

public class BanPanel {
	private String playerName;
	private static IconMenu selectMode;
	public static final String meta = "gameblade.banPanel";
	
	static {
		selectMode = new IconMenu("Select Mode", 9, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				int slot = event.getPosition();
				Mode mode = null;
				if(slot == 0){
					mode = Mode.BAN;
				}
				else if(slot == 1){
					mode = Mode.PROFILE;
				}
				else {
					event.setWillClose(false);
					event.setWillDestroy(false);
					return;
				}
				
				BanPanel bp = get(event.getPlayer());
				if(bp != null){
					bp.selected(event.getPlayer(), mode);
				}
				
				event.setWillClose(true);
				event.setWillDestroy(false);
				return;
			}}, GameBlade.plugin);
		
		selectMode.setOption(0, new ItemStack(Material.STICK), ChatColor.GOLD+"Ban", ChatColor.RED+"Ban somebody");
		selectMode.setOption(1, new ItemStack(Material.PAPER), ChatColor.GOLD+"Profile", ChatColor.RED+"View a players' ban profile");
	}
	
	enum Mode {
		BAN, PROFILE;
	}
	
	public static BanPanel get(Player player){
		if(player.hasMetadata(BanPanel.meta)){
			Object o = player.getMetadata(BanPanel.meta).get(0).value();
			player.removeMetadata(BanPanel.meta, GameBlade.plugin);
			
			if(o instanceof BanPanel){
				return (BanPanel) o;
			}
		}
		
		return null;
	}
	
	private Mode mode = Mode.BAN;
	private String otherPlayer = null;
	
	public BanPanel(Player player){
		this.playerName = player.getName();
		player.setMetadata(meta, new MetaValue(this, GameBlade.plugin));
		selectMode.open(player);
	}
	
	private void selected(Player player, final Mode mode){
		this.mode = mode;
		new ChatInput(player, new InputValidator(){

			@Override
			public String getHelpMessage() {
				return ChatColor.GREEN+"Enter the name of the player into the chat";
			}

			@Override
			public boolean isValid(Player player, String input) {
				if(input.length() > 16 || input.length() < 1){
					return false;
				}
				if(input.contains(" ")){
					return false;
				}
				return true;
			}

			@Override
			public void onValidInput(Player player, String input) {
				onPlayerSelected(player, input);
			}

			@Override
			public String getInvalidMessage() {
				return ChatColor.RED+"Invalid player name!";
			}});
	}
	
	public void onPlayerSelected(Player admin, String name){
		this.otherPlayer = name;
		admin.sendMessage(ChatColor.GRAY+"Received: '"+name+"'");
		//TODO
	}
	
	public void destroy(){
		Player player = Bukkit.getPlayer(playerName);
		if(player != null){
			player.removeMetadata(BanPanel.meta, GameBlade.plugin);
		}
	}
	
	public void onClose(){
		destroy();
		//TODO
	}
}
