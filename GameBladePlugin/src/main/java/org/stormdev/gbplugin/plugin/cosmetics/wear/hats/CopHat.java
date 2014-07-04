package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CopHat extends BlockhatBase {
	private static String META = "cophatmeta";
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.STAINED_GLASS, 1, (byte)11);
	}

	@Override
	public String getID() {
		return "cophat";
	}

	@Override
	public double getPrice() {
		return 149;
	}

	@Override
	public String getUserFriendlyName() {
		return "Cop Hat (Changes colour)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP;
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
		if(i == null){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
		}
		
		if(i.getData().getData() == (byte) 3){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 14);
		}
		else if(i.getData().getData() == (byte) 14){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
		}
		
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
		super.remove(player);
	}

}
