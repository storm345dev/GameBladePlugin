package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PumpkinHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "pumpkinhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PUMPKIN);
	}

	@Override
	public double getPrice() {
		return 0;
	}

	@Override
	public String getUserFriendlyName() {
		return "Pumpkin Hat";
	}

}
