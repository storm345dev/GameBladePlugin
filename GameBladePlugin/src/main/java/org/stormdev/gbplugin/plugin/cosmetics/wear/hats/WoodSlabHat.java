package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class WoodSlabHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "woodslabhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.WOOD_STEP);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Wood Brick Slab Hat";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP_PLUS;
	}

}
