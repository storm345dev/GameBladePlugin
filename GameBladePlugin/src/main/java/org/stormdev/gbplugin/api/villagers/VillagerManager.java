package org.stormdev.gbplugin.api.villagers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.stormdev.gbapi.storm.misc.Colors;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class VillagerManager implements org.stormdev.gbapi.villagers.VillagerManager, Listener {
	public static VillagerManager instance = null;
	
	private Map<String, VillagerListener> registered = new HashMap<String, VillagerListener>();
	
	public VillagerManager(){
		instance = this;
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	@Override
	public void registerVillagerListener(String villagerName,
			VillagerListener listener)
			throws VillagerAlreadyRegisteredException {
		villagerName = ChatColor.stripColor(Colors.colorise(villagerName));
		if(isVillagerRegistered(villagerName)){
			throw new VillagerAlreadyRegisteredException();
		}
		
		registered.put(villagerName, listener);
	}

	@Override
	public boolean isVillagerRegistered(String name) {
		if(name == null){
			return false;
		}
		name = ChatColor.stripColor(Colors.colorise(name));
		return registered.containsKey(name);
	}

	@Override
	public Villager spawnVillager(String villagerName, Location location) {
		if(!Bukkit.isPrimaryThread()){
			throw new RuntimeException("ASYNC VILLAGERS OMFG ;(");
		}
		Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		if(villager == null){
			return null;
		}
		
		villager.setAdult();
		villager.setAgeLock(true);
		villager.setBreed(false);
		villager.setCanPickupItems(false);
		villager.setCustomName(ChatColor.GOLD+villagerName);
		villager.setCustomNameVisible(true);
		villager.setProfession(Profession.BUTCHER);
		villager.setRemoveWhenFarAway(false);
		return villager;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	void onHurt(EntityDamageEvent event){
		Entity hurt = event.getEntity();
		if(!(hurt instanceof Villager)){
			return;
		}
		
		Villager villager = (Villager) hurt;
		if(villager.getCustomName() == null){
			return;
		}
		if(!isVillagerRegistered(villager.getCustomName())){
			return;
		}
		
		event.setCancelled(true);
		event.setDamage(0);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	void onInteract(PlayerInteractEntityEvent event){
		if(event.isCancelled()){
			return;
		}
		Entity hurt = event.getRightClicked();
		if(!(hurt instanceof Villager)){
			return;
		}
		
		Villager villager = (Villager) hurt;
		if(!isVillagerRegistered(villager.getCustomName())){
			return;
		}
		
		VillagerListener vl = registered.get(ChatColor.stripColor(Colors.colorise(villager.getCustomName())));
		if(vl == null){
			return;
		}
		
		event.setCancelled(true);
		
		vl.onInteract(event.getPlayer());
	}
	
	
}
