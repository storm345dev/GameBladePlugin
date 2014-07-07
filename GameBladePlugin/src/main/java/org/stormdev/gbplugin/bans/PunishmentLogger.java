package org.stormdev.gbplugin.bans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.bans.PunishmentLogs;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class PunishmentLogger implements PunishmentLogs {
	
	private static PunishmentLogger instance = null;
	
	private static final String SQL_TABLE = "punish_logs";
	private static final String SQL_ID = "id";
	private static final String SQL_BANS = "bans";
	private static final String SQL_KICKS = "kicks";
	private static final String SQL_WARNS = "warns";
	private static final String SPLITTER = "<,>";
	
	
	public PunishmentLogger(){
		GameBlade.plugin.GBSQL.createTable(SQL_TABLE, new String[]{SQL_ID, SQL_BANS, SQL_KICKS, SQL_WARNS}, 
				new String[]{"varchar(255) NOT NULL PRIMARY KEY", "longtext", "longtext", "longtext"});
	}
	
	public static PunishmentLogger getInstance(){
		if(instance == null){instance = new PunishmentLogger();}
		return instance;
	}
	
	private String getSQLColumn(PunishmentType type){
		switch(type){
		case BAN: return SQL_BANS;
		case KICK: return SQL_KICKS;
		case WARN: return SQL_WARNS;
		default: return SQL_BANS;
		}
	}
	
	private String getTimeString(){
		Calendar now = Calendar.getInstance();
		return "["+now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.MONTH)+"/"+now.get(Calendar.YEAR)+" @ "
				+now.get(Calendar.HOUR_OF_DAY) +":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)+"]";
	}

	@Override
	public void log(final Player player, final PunishmentType type, final String reason,
			final String admin) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				log(PlayerIDFinder.getMojangID(player).getID(), type, reason, admin);
				return;
			}});
	}

	@Override
	public List<PunishmentLog> getLogs(Player player, PunishmentType type) {
		notSync();
		return getLogs(PlayerIDFinder.getMojangID(player).getID(), type);
	}

	@Override
	public void log(final String uuid, final PunishmentType type, final String reason,
			final String admin) {
		runAsync(new Runnable(){
			@Override
			public void run() {
				GameBlade.logger.info("Logging "+type.getUserFriendlyName()+" of "+uuid);
				List<PunishmentLog> logs = getLogs(uuid, type);
				if(logs == null){
					logs = new ArrayList<PunishmentLog>();
				}
				logs.add(new PunishmentLog(type, reason, admin, getTimeString()));
				
				StringBuilder toSQL = new StringBuilder();
				for(PunishmentLog log:logs){
					String s = log.toExternalString();
					if(toSQL.length()>0){
						toSQL.append(SPLITTER);
					}
					s.replaceAll(Pattern.quote(SPLITTER), "");
					toSQL.append(s);
				}
				
				try {
					GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, SQL_ID, uuid, getSQLColumn(type), toSQL.toString());
				} catch (SQLException e) {
					//Crap
					e.printStackTrace();
				}
				return;
			}});
	}

	@Override
	public List<PunishmentLog> getLogs(String uuid, PunishmentType type) {
		notSync();
		try {
			Object SQL = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, SQL_ID, uuid, getSQLColumn(type));
			if(SQL == null || SQL.toString().equals("null")){
				return new ArrayList<PunishmentLog>();
			}
			
			String str = SQL.toString();
			List<PunishmentLog> results = new ArrayList<PunishmentLog>();
			String[] parts = str.split(Pattern.quote(SPLITTER));
			
			for(String part:parts){
				PunishmentLog log = PunishmentLog.fromString(part);
				if(log != null){
					results.add(log);
				}
			}
			
			return results;
		} catch (SQLException e) {
			//OH NO
			e.printStackTrace();
		}
		return new ArrayList<PunishmentLog>();
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
