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
import org.stormdev.gbapi.cosmetics.Currency;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbapi.storm.tokens.Tokens.TokenServiceUnavailableException;
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
	
	public void purchaseCosmetic(final Player player, final Cosmetic c){
		player.sendMessage("debug: "+c.getID());
		Rank playerRank = Rank.getRank(player);
		
		if(!playerRank.canUse(c.minimumRank())){	
			player.sendMessage(ChatColor.RED+"Sorry that item is "+c.minimumRank().getName()+" and higher only!");
			return;
		}
		player.sendMessage(ChatColor.GRAY+"Purchasing...");
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				List<String> owned = getOwnedCosmeticIds(player);
				if(owned.contains(c.getID())){
					player.sendMessage(ChatColor.RED+"You already own this item!");
					return;
				}
				if(c.getPrice() > 0){
					Currency currency = c.getCurrencyUsed();
					if(currency.equals(Currency.STARS)){
						int bal = GameBlade.api.getStarsHandler().getStars(player);
						if(bal < c.getPrice()){
							player.sendMessage(ChatColor.RED+"You cannot afford this item!");
							return;
						}
						GameBlade.api.getStarsHandler().takeStars(player, (int)(Math.ceil(c.getPrice())));
						player.sendMessage(ChatColor.RED+"-"+(int)(Math.ceil(c.getPrice()))+ChatColor.YELLOW+" stars");
					}
					else if(currency.equals(Currency.TOKENS)){
						int bal;
						try {
							bal = GameBlade.api.getTokenHandler().getTokens(player);
						} catch (TokenServiceUnavailableException e) {
							//Oh poo
							player.sendMessage(ChatColor.RED+"This server has lost connection to tokens! Please try on another!");
							return;
						}
						if(bal < c.getPrice()){
							player.sendMessage(ChatColor.RED+"You cannot afford this item!");
							return;
						}
						GameBlade.api.getTokenHandler().takeTokens(player, (int)(Math.ceil(c.getPrice())));
						player.sendMessage(ChatColor.RED+"-"+(int)(Math.ceil(c.getPrice()))+ChatColor.YELLOW+" tokens");
					}
				}
				//We've taken their money, now to give them the stuff
				
				//Give the item
				awardCosmetic(player, c, owned);
				
				Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

					@Override
					public void run() {
						postPurchase(player, c);
						return;
					}});
				return;
			}});
	}
	
	public void awardCosmetic(Player player, Cosmetic c, List<String> owned){
		notSync();
		owned.add(c.getID());
		StringBuilder sb = new StringBuilder();
		for(String id:owned){
			if(sb.length() < 1){
				sb.append(id);
				continue;
			}
			sb.append("|").append(id);
		}
		
		try {
			GameBlade.plugin.GBSQL.setInTable(SQL_TABLE, SQL_ID_KEY, PlayerIDFinder.getMojangID(player).getID(), SQL_COSMETICS_KEY, sb.toString());
		} catch (SQLException e) {
			// Oh crap
			e.printStackTrace();
			if(c.getCurrencyUsed().equals(Currency.TOKENS)){
				GameBlade.api.getTokenHandler().awardTokens(player, (int)(Math.ceil(c.getPrice())));
			}
			else if(c.getCurrencyUsed().equals(Currency.STARS)){
				GameBlade.api.getStarsHandler().awardStars(player, (int)(Math.ceil(c.getPrice())));
			}
			player.sendMessage(ChatColor.RED+"Awfully sorry, your transaction failed to complete. But we've refunded the cost.");
			player.sendMessage(ChatColor.GREEN+"Refunded!");
			return;
		}
	}
	
	public void postPurchase(Player player, Cosmetic c){
		player.sendMessage(ChatColor.GREEN+"Successfully purchased item!");
		//TODO ?
	}
	
	public List<String> getOwnedCosmeticIds(Player player){
		notSync();
		//Use SQL
		String uuid = PlayerIDFinder.getMojangID(player).getID();
		String owned;
		
		try {
			Object o = GameBlade.plugin.GBSQL.searchTable(SQL_TABLE, SQL_ID_KEY, uuid, SQL_COSMETICS_KEY);
			if(o != null){
				owned = o.toString();
			}
			else {
				owned = null;
			}
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
