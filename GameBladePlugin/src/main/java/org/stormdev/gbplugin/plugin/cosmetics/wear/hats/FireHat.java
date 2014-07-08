package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class FireHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "firehat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.FIRE);
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Fire Hat (Worn on back)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM_PLUS;
	}

}
