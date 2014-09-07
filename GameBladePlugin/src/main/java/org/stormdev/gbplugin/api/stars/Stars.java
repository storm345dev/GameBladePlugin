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
		return getStars(uuid);
	}
	
	@Override
	public void awardStars(final Player player, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				awardStars(uuid, stars);
				return;
			}});
	}
	@Override
	public void takeStars(final Player player, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				takeStars(uuid, stars);
				return;
			}});
	}
	@Override
	public void setStars(final Player player, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				setStars(uuid, stars);
				return;
			}});
	}
	
	private void runAsync(Runnable run){
		if(!(Bukkit.isPrimaryThread())){
			run.run();
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, run);
	}



	@Override
	public int getStars(String uuid) {
		if(Bukkit.isPrimaryThread()){
			throw new RuntimeException("NO synchronous star lookups!");
		}
		try {
			int amt = Integer.parseInt(GameBlade.plugin.GBSQL.searchTable("stars", "uuid", uuid, "stars").toString());
			if(amt < 0){
				setStars(uuid, 0);
				amt = 0;
			}
			return amt;
		} catch (Exception e) {
			//They have no stars e.printStackTrace();
		}
		return 0;
	}



	@Override
	public void awardStars(final String uuid, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				int o = getStars(uuid);
				int i = o+stars;
				try {
					GameBlade.plugin.GBSQL.setInTable("stars", "uuid", uuid, "stars", i);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}



	@Override
	public void takeStars(final String uuid, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
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
	public void setStars(final String uuid, final int stars) {
		runAsync(new Runnable(){

			@Override
			public void run() {
				try {
					GameBlade.plugin.GBSQL.setInTable("stars", "uuid", uuid, "stars", stars);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}
}
