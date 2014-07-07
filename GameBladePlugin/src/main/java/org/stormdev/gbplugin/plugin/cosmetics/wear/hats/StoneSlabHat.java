package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class StoneSlabHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "stoneslabhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.STEP);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Stone Slab Hat (Like Mask)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP;
	}

}
