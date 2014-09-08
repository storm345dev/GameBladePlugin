package org.stormdev.gbplugin.plugin.ranks;

import java.sql.SQLException;

import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbapi.storm.misc.Sch;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class RankSQL {
	public static String SQL_TABLE_NAME = "playerdata";
	public static String SQL_ID_COLUMN = "id";
	public static String SQL_RANK_COLUMN = "rank";
	
	public static void setRankByUUID(String uuid, Rank rank){
		Sch.notSync();
		try {
			GameBlade.plugin.GBSQL.setInTable(SQL_TABLE_NAME, SQL_ID_COLUMN, uuid, SQL_RANK_COLUMN, rank.getDbId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Rank getRankByUUID(String uuid){
		Sch.notSync();
		try {
			Object o = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE_NAME, SQL_ID_COLUMN, uuid, SQL_RANK_COLUMN);
			if(o != null){
				String s = o.toString();
				try {
					Rank r = Rank.getRank(Integer.parseInt(s));
					if(r != null){
						return r;
					}
				} catch (NumberFormatException e) {
					//Not valid
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Rank.DEFAULT;
	}
	
	public static void setRankByName(String name, Rank rank){
		Sch.notSync();
		String uuid = PlayerIDFinder.getMojangID(name).getID();
		setRankByUUID(uuid, rank);
	}
}
