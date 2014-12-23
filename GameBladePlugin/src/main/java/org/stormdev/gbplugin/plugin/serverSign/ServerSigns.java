package org.stormdev.gbplugin.plugin.serverSign;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.stormdev.mario.utils.Colors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.stormdev.gbapi.core.APIProvider;
import org.stormdev.gbapi.storm.misc.Sch;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.server.ping.ServerListPing;
import org.stormdev.gbplugin.plugin.server.ping.ServerListPing.StatusResponse;

public class ServerSigns implements Listener {
	private List<ServerSign> signs = new ArrayList<ServerSign>();
	public static long UPDATE_DELAY = 15 * 20l; //15 seconds
	private File saveFile;
	
	public ServerSigns(File saveFile){
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
		this.saveFile = saveFile;
		this.saveFile.getParentFile().mkdirs();
		if(!this.saveFile.exists()){
			try {
				this.saveFile.createNewFile();
			} catch (IOException e) {
				//Lol, errors will happen
				GameBlade.logger.info("Failed to create the save file for Server Signs!");
			}
		}
		load();
		Bukkit.getScheduler().runTaskTimerAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				asyncSignUpdate();
				return;
			}}, 0l, UPDATE_DELAY);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	void onBlockRemove(BlockBreakEvent event){
		if(event.isCancelled()){
			return;
		}
		
		Block block = event.getBlock();
		if(!(block.getState() instanceof Sign)){
			return;
		}
		
		//It's a sign
		if(!isSign(block.getLocation())){
			return; //It's not a server sign...
		}
		//It's a server sign
		if(removeSign(block.getLocation()) && event.getPlayer() != null){
			event.getPlayer().sendMessage(ChatColor.GREEN+"Successfully unregistered server sign!");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	void signPlace(SignChangeEvent event){
		if(event.isCancelled()){
			return;
		}
		
		String[] lines = event.getLines();
		if(lines.length < 1){
			return;
		}
		if(!lines[0].equalsIgnoreCase("[ServerSign]")){
			return;
		}
		Player player = event.getPlayer();
		Location loc = event.getBlock().getLocation();
		player.sendMessage(ChatColor.GRAY+"Launching creator...");
		ServerSignCreator.runCreator(player, loc);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	void signClick(PlayerInteractEvent event){
		if(event.isCancelled()){
			return;
		}
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getClickedBlock() == null){
			return;
		}
		Block clicked = event.getClickedBlock();
		if(!(clicked.getState() instanceof Sign)){
			return;
		}
		ServerSign sign = getSign(clicked.getLocation());
		if(sign == null){
			return;
		}
		
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.GRAY+"Connecting...");
		APIProvider.getAPI().sendToServer(player, sign.getBungeeServerName());
		return;
	}
	
	private void asyncSignUpdate(){
		if(signCount() < 1){
			return; //No signs to update
		}
		for(final ServerSign sign:getSigns()){ //For each sign
			String name = (ChatColor.BLUE+sign.getDisplayName());
			if(name.length() >= 15){
				name = name.substring(0, 15);
			}
			String MOTD = ChatColor.RED+"Restarting";
			int playerCount = 0;
			int maxPlayers = 0;
			boolean online = true;
			
			//Get data
			try {
				StatusResponse sr = ServerListPing.fetchData(new InetSocketAddress(sign.getIpNoPort(), sign.getPort()));
				MOTD = Colors.colorise(sr.getDescription());
				if(MOTD.length() >= 15){
					MOTD = MOTD.substring(0, 15);
				}
				playerCount = sr.getPlayers().getOnline();
				maxPlayers = sr.getPlayers().getMax();
			} catch (IOException e) {
				//It's offline
				online = false;
			}
			
			final String sName = name;
			final String motd = MOTD;
			final String players = ChatColor.GREEN+"["+playerCount+"/"+maxPlayers+"]";
			final boolean isOnline = online;
			
			Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					Location loc = sign.getLoc();
					Block block = loc.getBlock();
					if(!(block.getState() instanceof Sign)){
						removeSign(sign);
						GameBlade.logger.info("Unregistered server sign which is no longer a sign!");
						return;
					}
					Sign s = (Sign) block.getState();
					s.setLine(1, sName);
					s.setLine(2, motd);
					if(isOnline){
						s.setLine(3, players);
					}
					s.update(false);
					return;
				}});
		}
	}
	
	public int signCount(){
		return signs.size();
	}
	
	public void addSign(ServerSign s){
		signs.add(s);
		asyncSave();
	}
	
	public void removeSign(ServerSign s){
		signs.remove(s);
		asyncSave();
	}
	
	public List<ServerSign> getSigns(){
		return new ArrayList<ServerSign>(signs); //A clone of the REAL list
	}
	
	public boolean removeSign(Location signLoc){
		boolean success = false;
		for(ServerSign s:getSigns()){
			Location sl = s.getLoc();
			if(sl.getBlockX() == signLoc.getBlockX() && sl.getBlockY() == signLoc.getBlockY() && sl.getBlockZ() == signLoc.getBlockZ()){
				removeSign(s);
				success = true;
			}
		}
		return success;
	}
	
	public boolean isSign(Location signLoc){
		return getSign(signLoc) != null;
	}
	
	public ServerSign getSign(Location signLoc){
		for(ServerSign s:getSigns()){
			Location sl = s.getLoc();
			if(sl.getBlockX() == signLoc.getBlockX() && sl.getBlockY() == signLoc.getBlockY() && sl.getBlockZ() == signLoc.getBlockZ()){
				return s;
			}
		}
		return null;
	}
	
	private void load(){
		ObjectInput oi = null;
		try {
			oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(saveFile)));
			try {
				@SuppressWarnings("unchecked")
				List<ServerSign> ss = (List<ServerSign>) oi.readObject();
				if(ss != null){
					ServerSigns.this.signs = ss;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				GameBlade.logger.info("Unable to de-serialize server signs from save file!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			if(oi != null){
				try {
					oi.close();  //Avoid memory leaks
				} catch (IOException e) {
					//Error closing the stream
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveSync(){
		ObjectOutput oo = null;
		try {
			oo = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)));
			oo.writeObject(signs);
			oo.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			if(oo != null){
				try {
					oo.close();  //Avoid memory leaks
				} catch (IOException e) {
					//Error closing the stream
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void asyncSave(){
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				saveSync();
				return;
			}});
	}
}
