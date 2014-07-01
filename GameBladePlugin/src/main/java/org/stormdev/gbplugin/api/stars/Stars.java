package org.stormdev.gbplugin.api.stars;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class Stars implements org.stormdev.gbapi.stars.Stars{
	private static Stars instance;
	public static Stars getInstance(){
		if(instance == null){
			instance = new Stars();
		}
		return instance;
	}
	
	
	
	@Override
	public int getStars(Player player) {
		if(Bukkit.isPrimaryThread()){
			throw new RuntimeException("Do not lookup player's star balances in the main thread!");
		}
		
		String uuid = PlayerIDFinder.getMojangID(player).getID();
		try {
			return Integer.parseInt(GameBlade.plugin.GBSQL.searchTable("stars", "uuid", uuid, "stars").toString());
		} catch (Exception e) {
			GameBlade.logger.info("Uh oh! Stars lookup error!");
			e.printStackTrace();
		}
		return 0;
	}
	
	private int getStars(String uuid){
		try {
			return Integer.parseInt(GameBlade.plugin.GBSQL.searchTable("stars", "uuid", uuid, "stars").toString());
		} catch (Exception e) {
			GameBlade.logger.info("Uh oh! Stars lookup error!");
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void awardStars(final Player player, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				int i = getStars(uuid)+stars;
				try {
					GameBlade.plugin.GBSQL.setInTable("stars", "uuid", uuid, "stars", i);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}
	@Override
	public void takeStars(final Player player, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				int i = getStars(uuid)-stars;
				if(i < 0){
					i = 0;
				}
				try {
					GameBlade.plugin.GBSQL.setInTable("stars", "uuid", uuid, "stars", i);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}
	@Override
	public void setStars(final Player player, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				try {
					GameBlade.plugin.GBSQL.setInTable("stars", "uuid", uuid, "stars", stars);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}
	
	private void runAsync(Runnable run){
		if(!(Bukkit.isPrimaryThread())){
			run.run();
		}
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, run);
	}
}
