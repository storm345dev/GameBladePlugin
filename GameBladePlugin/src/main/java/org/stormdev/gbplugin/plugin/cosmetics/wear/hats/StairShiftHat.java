package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class StairShiftHat extends ChangingHat {
	private static String META = "stairshifthatmeta";
	
	private List<Material> stairMats = new ArrayList<Material>();
	private int pos = 0;
	
	public StairShiftHat(){
		for(Material mat:Material.values()){
			if(!stairMats.contains(mat) && mat.name().toLowerCase().contains("stair")){
				stairMats.add(mat);
			}
		}
	}
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}

	@Override
	public String getID() {
		return "stairshifthat";
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Stair Shift Hat (Changes stairs)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM;
	}
	
	public void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null || (i.getType().equals(Material.PAPER))){
			i = new ItemStack(Material.WOOD_STAIRS);
		}
		
		pos++;
		if(pos >= stairMats.size()){
			pos = 0;
		}
		
		i = new ItemStack(stairMats.get(pos));
		
		player.getInventory().setHelmet(i);
	}

	@Override
	public String getMeta() {
		return META;
	}

}
