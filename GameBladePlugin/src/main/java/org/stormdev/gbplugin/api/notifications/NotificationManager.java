package org.stormdev.gbplugin.api.notifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.stormdev.gbapi.notifications.Notifications;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbapi.storm.misc.Colors;
import org.stormdev.gbapi.storm.misc.Sch;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class NotificationManager implements Listener,Notifications {
	
	public static String TABLE = "notifications";
	public static String UUID = "id";
	public static String NOTIFICATIONS = "notifications";
	
	public static String SEPARATOR = "▼";
	
	public NotificationManager(){
		GameBlade.plugin.GBSQL.createTable(TABLE, new String[]{UUID, NOTIFICATIONS}, new String[]{"varchar(255) NOT NULL PRIMARY KEY", "longtext"});
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	@EventHandler
	void login(PlayerJoinEvent event){
		showNotifications(event.getPlayer());
	}
	
	@Override
	public void showNotifications(final Player player){
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				try {
					String uuid = PlayerIDFinder.getMojangID(player).getID();
					List<String> notifys = getNotifications(uuid);
					if(notifys.size() > 0){
						if(player != null && player.isOnline()){
							for(String notify:notifys){
								String s = ChatColor.AQUA+"[Alert]"+ChatColor.GREEN+Colors.colorise(notify);
								player.sendMessage(s);
							}
							clearNotifications(uuid);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}});
	}
	
	@Override
	public void addNotification(final String uuid, final String notification){
		try {
			Player player = Bukkit.getPlayer(PlayerIDFinder.getAsUUID(uuid));
			if(player != null && player.isOnline()){
				String s = ChatColor.AQUA+"[Alert]"+ChatColor.GREEN+Colors.colorise(notification);
				player.sendMessage(s);
				return;
			}
		} catch (Exception e) {
		}
		
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				List<String> notifys = getNotifications(uuid);
				notifys.add(notification);
				setNotifications(uuid, notifys);
				return;
			}});
	}
	
	@Override
	public void clearNotifications(String uuid){
		setNotifications(uuid, new ArrayList<String>());
	}
	
	@Override
	public void setNotifications(final String uuid, final List<String> notifications){
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				try {
					if(notifications.size() < 1){
						GameBlade.plugin.GBSQL.deleteFromTable(TABLE, UUID, uuid);
						return;
					}
					StringBuilder n = new StringBuilder();
					for(String not:notifications){
						if(n.length() > 0){
							n.append(SEPARATOR);
						}
						n.append(not);
					}
					GameBlade.plugin.GBSQL.setInTable(TABLE, UUID, uuid, NOTIFICATIONS, n.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}});
	}
	
	@Override
	public List<String> getNotifications(String uuid){
		Sch.notSync();
		try {
			String raw = GameBlade.plugin.GBSQL.searchTable(TABLE, UUID, uuid, NOTIFICATIONS).toString();
			return new ArrayList<String>(Arrays.asList(raw.split(Pattern.quote(SEPARATOR))));
		} catch (Exception e){
			return new ArrayList<String>();
		}
	}
}
