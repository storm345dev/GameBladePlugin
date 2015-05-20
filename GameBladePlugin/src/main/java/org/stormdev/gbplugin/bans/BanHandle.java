package org.stormdev.gbplugin.bans;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.bans.BanHandler;
import org.stormdev.gbapi.bans.PunishmentLogs.PunishmentType;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbapi.storm.misc.Colors;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class BanHandle implements BanHandler {
	
	private static final String SQL_TABLE = "bans";
	private static final String ID_COLUMN = "id";
	private static final String BANNED_BY_COLUMN = "banned_by";
	private static final String REASON_COLUMN = "reason";
	private static final String TIME_COLUMN = "time";
	
	private static final String PLAYER_DATA_SQL_TABLE = "playerdata";
	private static final String PLAYER_DATA_BANS_COLUMN = "bans";
	private static final String PLAYER_DATA_ID_COLUMN = "id";
	
	public BanHandle(){
		GameBlade.plugin.GBSQL.createTable(SQL_TABLE, new String[]{
				ID_COLUMN, BANNED_BY_COLUMN, REASON_COLUMN, TIME_COLUMN}, new String[]{
				"varchar(255) NOT NULL PRIMARY KEY", "varchar(16)", "varchar(255)", "varchar(255)"
		});
	}
	
	@Override
	public void unban(final String uuid){
		runAsync(new Runnable(){

			@Override
			public void run() {
				GameBlade.logger.info("Unbanning: "+uuid);
				try {
					GameBlade.plugin.GBSQL.deleteFromTable(SQL_TABLE, ID_COLUMN, uuid);
					//GameBlade.plugin.GBSQL.exec("DELETE FROM bans WHERE bans.id = '"+uuid+"'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}
	
	@Override
	public boolean isBanned(Player player) {
		notSync();
		Time t = getBanDuration(player);
		if(t == null){
			return false;
		}
		if(!t.hasElapsed()){
			return true;
		}
		
		unban(PlayerIDFinder.getMojangID(player).getID());
		return false;
	}
	
	@Override
	public boolean isBanned(String uuid) {
		notSync();
		Time t = getBanDuration(uuid);
		if(t == null){
			return false;
		}
		if(!t.hasElapsed()){
			return true;
		}
		
		unban(uuid);
		return false;
	}

	@Override
	public String getBanReason(Player player) {
		notSync();
		String uuid = PlayerIDFinder.getMojangID(player).getID();
		try {
			String s = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, ID_COLUMN, uuid, REASON_COLUMN).toString();
			if(s == null || s.equalsIgnoreCase("null")){
				return null;
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getBanReason(String uuid) {
		notSync();
		try {
			String s = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, ID_COLUMN, uuid, REASON_COLUMN).toString();
			if(s == null || s.equalsIgnoreCase("null")){
				return null;
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Time getBanDuration(Player player) {
		notSync();
		String uuid = PlayerIDFinder.getMojangID(player).getID();
		try {
			String s = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, ID_COLUMN, uuid, TIME_COLUMN).toString();
			if(s == null || s.equalsIgnoreCase("null")){
				return null;
			}
			return Time.fromString(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Time getBanDuration(String uuid) {
		notSync();
		try {
			String s = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, ID_COLUMN, uuid, TIME_COLUMN).toString();
			if(s == null || s.equalsIgnoreCase("null")){
				return null;
			}
			return Time.fromString(s);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void ban(final Player player, final Player admin, final String reason, final Time time) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String banned_uuid = PlayerIDFinder.getMojangID(player).getID();
				try {
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, ID_COLUMN, banned_uuid, BANNED_BY_COLUMN, admin.getName());
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, ID_COLUMN, banned_uuid, REASON_COLUMN, reason);
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, ID_COLUMN, banned_uuid, TIME_COLUMN, time);
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
				
				int bans = 0;
				try {
					bans = Integer.parseInt(GameBlade.plugin.GBSQL.searchTable(PLAYER_DATA_SQL_TABLE, PLAYER_DATA_ID_COLUMN, banned_uuid, PLAYER_DATA_BANS_COLUMN).toString());
				} catch (Exception e) {
					//No bans set
				}
				
				bans++;
				if(bans < 1){
					GameBlade.logger.info("ERROR with incrementing players' ban amount!");
				}
				try {
					GameBlade.plugin.GBSQL.setInTable(PLAYER_DATA_SQL_TABLE, PLAYER_DATA_ID_COLUMN, banned_uuid, PLAYER_DATA_BANS_COLUMN, bans+"");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				//Log it
				GameBlade.api.getPunishmentLogger().log(banned_uuid, PunishmentType.BAN, reason, admin.getName());
				
				if(player != null && player.isOnline()){
					Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

						@Override
						public void run() {
							player.kickPlayer("Banned: "+Colors.colorise(reason));
							return;
						}});
				}
				
				return;
			}});
	}
	
	@Override
	public void ban(final String banned_uuid, final Player admin, final String reason, final Time time) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				try {
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, ID_COLUMN, banned_uuid, BANNED_BY_COLUMN, admin.getName());
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, ID_COLUMN, banned_uuid, REASON_COLUMN, reason);
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, ID_COLUMN, banned_uuid, TIME_COLUMN, time);
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
				
				int bans = 0;
				try {
					bans = Integer.parseInt(GameBlade.plugin.GBSQL.searchTable(PLAYER_DATA_SQL_TABLE, PLAYER_DATA_ID_COLUMN, banned_uuid, PLAYER_DATA_BANS_COLUMN).toString());
				} catch (Exception e) {
					//No bans set
				}
				
				bans++;
				try {
					GameBlade.plugin.GBSQL.setInTable(PLAYER_DATA_SQL_TABLE, PLAYER_DATA_ID_COLUMN, banned_uuid, PLAYER_DATA_BANS_COLUMN, bans);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				//Log it
				GameBlade.api.getPunishmentLogger().log(banned_uuid, PunishmentType.BAN, reason, admin.getName());
				
				return;
			}});
	}

	@Override
	public String getWhoBanned(String uuid) {
		notSync();
		try {
			String who = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, ID_COLUMN, uuid, BANNED_BY_COLUMN).toString();
			if(who == null || who.equalsIgnoreCase("null")){
				return null;
			}
			return who;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void notSync(){
		if(Bukkit.isPrimaryThread()){
			throw new RuntimeException("Do NOT use asynchronously!");
		}
	}
	
	private void runAsync(Runnable run){
		if(!Bukkit.isPrimaryThread()){
			run.run();
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, run);
	}

}
