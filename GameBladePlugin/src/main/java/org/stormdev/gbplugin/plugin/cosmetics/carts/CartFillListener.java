package org.stormdev.gbplugin.plugin.cosmetics.carts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbplugin.plugin.core.Config;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CartFillListener implements Listener {
	private static final String SETTING_META = "cardisplay.setting";
	private static final String SET_META = "cardisplay.set";
	
	public CartFillListener(){
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	void onVehicleEntry(VehicleEnterEvent event){
		if(!Config.enableFancyCars.getValue()){
			return;
		}
		if(event.isCancelled()){
			return;
		}
		
		Entity e = event.getEntered();
		if(!(e instanceof Player)){
			return;
		}
		
		Player player = (Player) e;
		
		Entity m = event.getVehicle();
		if(!(m instanceof Minecart)){
			return;
		}
		
		Minecart mi = (Minecart) m;
		
		fillCar(mi, player);
	}
	
	@EventHandler
	void onVehicleExit(VehicleExitEvent event){
		if(!Config.enableFancyCars.getValue()){
			return;
		}
		if(event.isCancelled()){
			return;
		}
		
		Entity m = event.getVehicle();
		if(!(m instanceof Minecart)){
			return;
		}
		
		clearCar((Minecart) m);
	}
	
	private void clearCar(Minecart m){
		long start = System.currentTimeMillis();
		while(m.hasMetadata(SETTING_META)
				&& (System.currentTimeMillis() - start) < 10000){
			//Wait
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Oh well
			}
		}
		
		if(m.hasMetadata(SET_META)){
			//Clear it
			CartFiller.putBlockInCar(m, 0, 0);
			m.removeMetadata(SET_META, GameBlade.plugin);
		}
	}
	
	private void fillCar(Minecart m, Player player){
		m.removeMetadata(SETTING_META, GameBlade.plugin);
		m.setMetadata(SETTING_META, new MetadataValue(true, GameBlade.plugin));
		
		try{
			
			//TODO Fill IF they deserve it
			CartFiller.putBlockInCar(m, 20, 0, 15);
			
			m.removeMetadata(SET_META, GameBlade.plugin);
			m.setMetadata(SET_META, new MetadataValue(true, GameBlade.plugin));
		}
		finally {
			m.removeMetadata(SETTING_META, GameBlade.plugin);
		}
	}
}
