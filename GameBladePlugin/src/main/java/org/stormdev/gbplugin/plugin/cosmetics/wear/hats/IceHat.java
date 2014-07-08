package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class IceHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "icehat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.ICE);
	}

	@Override
	public double getPrice() {
		return 49;
	}

	@Override
	public String getUserFriendlyName() {
		return "Ice Hat";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM;
	}

}
