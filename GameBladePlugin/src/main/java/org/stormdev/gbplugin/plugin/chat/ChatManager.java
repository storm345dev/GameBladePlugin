package org.stormdev.gbplugin.plugin.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.stormdev.chattranslator.api.MessageHandler;
import org.stormdev.chattranslator.api.TranslatorToolkit;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.core.Config;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.utils.Colors;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatManager implements Listener {
	public static ChatColor getMsgColour(Player player){
		if(player.hasPermission("mta.chatcolor.staff")){
			return ChatColor.AQUA;
		}
		else if(Rank.getRank(player).canUse(Rank.ULTIMATE)){
			return ChatColor.GOLD;
		}
		else if(player.hasPermission("mta.chatcolor.vip")){
			return ChatColor.WHITE;
		}
		else {
			return ChatColor.GRAY;
		}
	}
	
	public ChatManager(){
		if(!Config.overrideChat.getValue()){ //DONT override the chat
			return;
		}
		
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
		TranslatorToolkit.getToolkit().setMessageHandler(new MessageHandler(){

			@Override
			public String formatMsg(Player player, String msg) {
				ChatColor color = getMsgColour(player);
				String sender = getSenderDisplayName(player);
				String message = ChatColor.WHITE + Colors.colorise(sender) + ChatColor.DARK_GRAY + " » " + color + msg;
				return message;
			}

			@Override
			public boolean overrideChatEvent() {
				return false;
			}

			@Override
			public void sendMessage(String msg, Player... recipients) {
				for(Player player:recipients){
					player.sendMessage(msg);
				}
			}});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	void onChat(AsyncPlayerChatEvent event){
		if(event.isCancelled()){
			return;
		}
		
		Player player = event.getPlayer();
		chat(player, event.getMessage());
		event.setCancelled(true);
	}
	
	private void chat(Player player, String msg){
		ChatColor color = getMsgColour(player);
		boolean colour = false;
		colour = player.hasPermission("mta.chat.colors") || Rank.getRank(player).canUse(Rank.ULTIMATE);
		msg = Colors.colorise(msg);
		if(!colour){
			msg = ChatColor.stripColor(msg);
		}
		
		String sender = getSenderDisplayName(player);
		String message = ChatColor.WHITE + Colors.colorise(sender) + ChatColor.DARK_GRAY + " » " + color + msg;
		//Bukkit.broadcastMessage(message);
		TranslatorToolkit.getToolkit().handleAsIfChatEvent((List<Player>) (new ArrayList<Player>(Bukkit.getOnlinePlayers())), player, msg);
		GameBlade.logger.info(message);
	}
	
	private String getSenderDisplayName(Player player){
		String fullName = getFullPrefixSuffixName(player);
		String dispName = fullName + "&f";

		String coloured = ChatColor.translateAlternateColorCodes('&', dispName);
		return coloured;
	}

	public String getFullPrefixSuffixName(Player player) {
		//String prefix = getSimplePrefixSuffix(player, "prefix");
		//String suffix = getSimplePrefixSuffix(player, "suffix");
		try {
			String prefix = getPEXPrefix(player);
			String suffix = getPEXSuffix(player);
			String name = player.getName();

			String full = prefix + name + suffix;

			return full;
		} catch (NoClassDefFoundError e) {
			String prefix = getSimplePrefixSuffix(player, "prefix");
			String suffix = getSimplePrefixSuffix(player, "suffix");
			String name = player.getName();

			String full = prefix + name + suffix;

			return full;
		}
	}
	
	public String getPEXPrefix(Player player){
		PermissionUser u = PermissionsEx.getUser(player);
		return u.getPrefix();
	}
	
	public String getPEXSuffix(Player player){
		PermissionUser u = PermissionsEx.getUser(player);
		return u.getSuffix();
	}

	public String getSimplePrefixSuffix(Player player, String type) {
		if (player.hasMetadata(type)) {
			return player.getMetadata(type).get(0).asString();
		}
		else{
			return "";
		}
	}
}
