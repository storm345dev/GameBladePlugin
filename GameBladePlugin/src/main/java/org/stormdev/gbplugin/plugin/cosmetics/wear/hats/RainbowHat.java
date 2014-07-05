package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class RainbowHat extends BlockhatBase {
	private static String META = "rainbowhatmeta";
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}

	@Override
	public String getID() {
		return "rainbowhat";
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Rainbow Hat (Changes colour)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM;
	}
	
	@Override
	public boolean apply(final Player player) {
		boolean b = super.apply(player);
		if(!b){
			return false;
		}
		
		BukkitTask t = Bukkit.getScheduler().runTaskTimer(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				switchHat(player);
				return;
			}}, 20l, 20l);
		player.removeMetadata(META, GameBlade.plugin);
		player.setMetadata(META, new MetadataValue(t, GameBlade.plugin));
		return true;
	}
	
	private void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null || !(i.getType().equals(Material.STAINED_GLASS))){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
		}
		
		int b = i.getData().getData();
		b++;
		if(b > 15){
			b = 0;
		}
		
		byte by = (byte) b;
		i = new ItemStack(Material.STAINED_GLASS, 1, (byte) by);
		
		player.getInventory().setHelmet(i);
	}
	
	@Override
	public void remove(Player player) {
		//Cancel task
		if(player.hasMetadata(META)){
			Object o = player.getMetadata(META).get(0).value();
			if(o instanceof BukkitTask){
				BukkitTask task = (BukkitTask) o;
				task.cancel();
			}
			player.removeMetadata(META, GameBlade.plugin);
		}
		player.getInventory().setHelmet(null);
	}

}
