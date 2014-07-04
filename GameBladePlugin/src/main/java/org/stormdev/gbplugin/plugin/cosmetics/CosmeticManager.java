package org.stormdev.gbplugin.plugin.cosmetics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.cosmetics.cosmetics.hats.HatRegistry;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.shop.CosmeticShop;

public class CosmeticManager {
	private static final String SQL_TABLE = "cosmetics";
	private static final String SQL_ID_KEY = "id";
	private static final String SQL_COSMETICS_KEY = "owned";
	
	private static CosmeticShop shop = null;
	private static Map<String, Cosmetic> cosmetics = new TreeMap<String, Cosmetic>();
	
	public static void registerCosmetic(Cosmetic cosmetic){
		cosmetics.put(cosmetic.getID(), cosmetic);
		if(shop != null){
			shop.updateShops();
		}
	}
	
	public CosmeticManager(){
		GameBlade.logger.info("Loading cosmetics...");
		HatRegistry.load();
		GameBlade.logger.info("Cosmetics loaded!");
		GameBlade.plugin.GBSQL.createTable(SQL_TABLE, new String[]{SQL_ID_KEY, SQL_COSMETICS_KEY}, new String[]{"varchar(255) NOT NULL PRIMARY KEY", "varchar(255)"});
		shop = new CosmeticShop(this);
	}
	
	public CosmeticShop getShop(){
		return shop;
	}
	
	public void registerACosmetic(Cosmetic cosmetic){
		cosmetics.put(cosmetic.getID(), cosmetic);
	}
	
	public List<Cosmetic> getAllByType(CosmeticType type){
		List<Cosmetic> results = new ArrayList<Cosmetic>();
		for(Cosmetic c:cosmetics.values()){
			if(c.getType().equals(type)){
				results.add(c);
			}
		}
		return results;
	}
	
	public void purchaseCosmetic(Player player, String cId){
		Cosmetic c = cosmetics.get(cId);
		if(c != null){
			purchaseCosmetic(player, c);
		}
	}
	
	public void purchaseCosmetic(Player player, Cosmetic c){
		player.sendMessage("debug: "+c.getID());
		Rank playerRank = Rank.getRank(player);
		
		if(!playerRank.canUse(c.minimumRank())){	
			player.sendMessage(ChatColor.RED+"Sorry that item is "+c.minimumRank().getName()+" and higher only!");
			return;
		}
		//TODO
	}
	
	public List<String> getOwnedCosmeticIds(Player player){
		notSync();
		//Use SQL
		String uuid = PlayerIDFinder.getMojangID(player).getID();
		String owned;
		
		try {
			owned = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, SQL_ID_KEY, uuid, SQL_COSMETICS_KEY).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		if(owned == null || owned.equalsIgnoreCase("null") || owned.length() < 1){
			return new ArrayList<String>();
		}
		
		List<String> o = new ArrayList<String>();
		String[] parts = owned.split(Pattern.quote("|"));
		for(String s:parts){
			if(s != null && s != "null" && s.length() > 0){
				o.add(s);
			}
		}
		
		return o;
	}
	
	private void notSync(){
		if(Bukkit.isPrimaryThread()){
			throw new RuntimeException("Must be used asynchronously!");
		}
	}
}
