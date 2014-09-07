package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class SlabShiftHat extends ChangingHat {
	private static String META = "slabshifthatmeta";
	
	private List<Material> mats = new ArrayList<Material>();
	private int pos = 0;
	
	public SlabShiftHat(){
		for(Material mat:Material.values()){
			if(!mats.contains(mat) && (mat.name().toLowerCase().contains("slab") || mat.name().toLowerCase().contains("step"))){
				mats.add(mat);
			}
		}
	}
	
	@Override
	public String getID() {
		return "slabshifthat";
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Slab Shift Hat (Changes slab)";
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
		if(pos >= mats.size()){
			pos = 0;
		}
		
		i = new ItemStack(mats.get(pos));
		
		player.getInventory().setHelmet(i);
	}

	@Override
	public String getMeta() {
		return META;
	}

}
