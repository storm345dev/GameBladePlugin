package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbapi.storm.misc.ObjectWrapper;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public abstract class ChangingHat extends BlockhatBase {
	public abstract String getMeta();
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}
	
	@Override
	public boolean apply(Player player) {
		boolean b = super.apply(player);
		if(!b){
			return false;
		}
		
		final String name = player.getName();
		
		final ObjectWrapper<BukkitTask> t = new ObjectWrapper<BukkitTask>();
		t.setValue(Bukkit.getScheduler().runTaskTimer(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				Player player = Bukkit.getPlayer(name);
				if(player == null || !player.isOnline()){
					t.getValue().cancel();
					return;
				}
				switchHat(player);
				return;
			}}, 40l, 40l));
		player.removeMetadata(getMeta(), GameBlade.plugin);
		player.setMetadata(getMeta(), new MetadataValue(t.getValue(), GameBlade.plugin));
		return true;
	}
	
	public abstract void switchHat(Player player);
	
	@Override
	public void remove(Player player) {
		//Cancel task
		if(player.hasMetadata(getMeta())){
			Object o = player.getMetadata(getMeta()).get(0).value();
			if(o instanceof BukkitTask){
				BukkitTask task = (BukkitTask) o;
				task.cancel();
			}
			player.removeMetadata(getMeta(), GameBlade.plugin);
		}
		player.getInventory().setHelmet(null);
	}

}
