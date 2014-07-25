package org.stormdev.gbplugin.plugin.cosmetics.carts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Currency;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.carts.BuyVehicleColoursMenu.ColourButton;
import org.stormdev.gbplugin.plugin.cosmetics.carts.ColouredVehicle.BlockVehicleColour;

public class Registry {
	public static void registerAll(BuyVehicleColoursMenu m){
		m.register(new ColourButton(new ItemStack(Material.STEP,1,(byte)7),
				"White Roof", 99, Currency.STARS, Rank.DEFAULT, "vc_rwhite"), 
				new BlockVehicleColour(new ItemStack(Material.STEP,1,(byte)7),31), 
				new ItemStack(Material.STEP,1,(byte)7));
	}
}
