package org.stormdev.gbplugin.plugin.serverSign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.stormdev.gbplugin.bans.ChatInput;
import org.stormdev.gbplugin.bans.ChatInput.InputValidator;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class ServerSignCreator {
	public static void create(Player player, Location signLoc, String displayName, String bungeeName, String mineManager, String ip, int port){
		ServerSign sign = new ServerSign(signLoc, displayName, bungeeName, mineManager, ip, port);
		GameBlade.plugin.serverSigns.addSign(sign);
		player.sendMessage(ChatColor.GREEN+"Successfully registered server sign!");
	}
	
	public static void runCreator(Player player, Location signLoc){
		new ChatInput(player, new FirstStage(signLoc));
	}
	
	private static class FirstStage implements InputValidator {

		private Location sign;
		
		private FirstStage(Location sign){
			this.sign = sign;
		}
		
		@Override
		public String getHelpMessage() {
			return ChatColor.AQUA+"Please type into the chat the display name of this server to show on the sign!";
		}

		@Override
		public boolean isValid(Player player, String input) {
			return true;
		}

		@Override
		public void onValidInput(final Player player, final String input) {
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					new ChatInput(player, new SecondStage(sign, input));
					return;
				}}, 3l);
		}

		@Override
		public String getInvalidMessage() {
			return "INVALID";
		}
		
	}
	
	private static class SecondStage implements InputValidator {

		private Location sign;
		private String displayName;
		
		private SecondStage(Location sign, String displayName){
			this.sign = sign;
			this.displayName = displayName;
		}
		
		@Override
		public String getHelpMessage() {
			return ChatColor.AQUA+"Please enter the bungeecord name for this server!";
		}

		@Override
		public boolean isValid(Player player, String input) {
			return true;
		}

		@Override
		public void onValidInput(final Player player, final String input) {
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					new ChatInput(player, new ThirdStage(sign, displayName, input));
					return;
				}}, 3l);
		}

		@Override
		public String getInvalidMessage() {
			return "INVALID";
		}
		
	}
	
	private static class ThirdStage implements InputValidator{

		private Location sign;
		private String display;
		private String bungee;
		
		private ThirdStage(Location sign, String displayName, String bungee){
			this.sign = sign;
			this.display = displayName;
			this.bungee = bungee;
		}
		
		@Override
		public String getHelpMessage() {
			return ChatColor.AQUA+"Please enter the MineManager ID of the server!";
		}

		@Override
		public boolean isValid(Player player, String input) {
			return true;
		}

		@Override
		public void onValidInput(final Player player, final String input) {
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					new ChatInput(player, new FourthStage(sign, display, bungee, input));
					return;
				}}, 3l);
		}

		@Override
		public String getInvalidMessage() {
			return "INVALID";
		}
		
	}
	
	private static class FourthStage implements InputValidator {

		private Location sign;
		private String display;
		private String bungee;
		private String mineManager;
		
		private FourthStage(Location sign, String display, String bungee, String mineManager){
			this.sign = sign;
			this.display = display;
			this.bungee = bungee;
			this.mineManager = mineManager;
		}
		
		@Override
		public String getHelpMessage() {
			return ChatColor.AQUA+"Please enter the IP of the server (WITHOUT port)!";
		}

		@Override
		public boolean isValid(Player player, String input) {
			return true;
		}

		@Override
		public void onValidInput(final Player player, final String input) {
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					new ChatInput(player, new FifthStage(sign, display, bungee, mineManager, input));
					return;
				}}, 3l);
		}

		@Override
		public String getInvalidMessage() {
			return "INVALID";
		}
		
	}
	
	private static class FifthStage implements InputValidator {

		private Location sign;
		private String display;
		private String bungee;
		private String mineManager;
		private String ip;
		
		private FifthStage(Location sign, String displayName, String bungeeName, String mineManager, String ip){
			this.sign = sign;
			this.display = displayName;
			this.bungee = bungeeName;
			this.mineManager = mineManager;
			this.ip = ip;
		}
		
		@Override
		public String getHelpMessage() {
			return ChatColor.AQUA+"Please enter the port of the server into the chat!";
		}

		@Override
		public boolean isValid(Player player, String input) {
			try {
				int i = Integer.parseInt(input);
				return i > 0;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public void onValidInput(final Player player, final String input) {
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					create(player, sign, display, bungee, mineManager, ip, Integer.parseInt(input));
					return;
				}}, 3l);
		}

		@Override
		public String getInvalidMessage() {
			return "The port must be a whole number greater than 0!";
		}
		
	}
}
