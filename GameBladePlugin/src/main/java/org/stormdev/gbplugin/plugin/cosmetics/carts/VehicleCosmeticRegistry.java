package org.stormdev.gbplugin.plugin.cosmetics.carts;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.stormdev.gbapi.cosmetics.Currency;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.carts.BuyVehicleColoursMenu.ColourButton;
import org.stormdev.gbplugin.plugin.cosmetics.carts.ColouredVehicle.BlockChangingVehicleColour;
import org.stormdev.gbplugin.plugin.cosmetics.carts.ColouredVehicle.BlockVehicleColour;

public class VehicleCosmeticRegistry {
	public static void registerAll(BuyVehicleColoursMenu m){
		for(int i=0;i<16;i++){
			ItemStack step = new ItemStack(Material.STEP, 1, (byte) i);
			int offset = 31;
			m.register(new ColourButton(step, "Slab Roof", 
					99, Currency.STARS, Rank.VIP, "vc_r"+i), new BlockVehicleColour(step, offset), step);
		}
		
		for(DyeColor color:DyeColor.values()){
			ColourButton but = new ColourButton(color, 199, Currency.STARS, Rank.VIP, "vc_"+color.name().toLowerCase());
			
			Wool w = new Wool();
			w.setColor(color);
			ItemStack glas = new ItemStack(Material.STAINED_CLAY, 1, w.getData());
			
			m.register(but, new BlockVehicleColour(glas, 5), glas);
		}
		
		m.register(new ColourButton(new ItemStack(Material.TNT),
				"TNT", 99, Currency.STARS, Rank.VIP_PLUS, "vc_tnt"), 
				new BlockChangingVehicleColour(5l, 
						new BlockVehicleColour(new ItemStack(Material.TNT), 5),
						new BlockVehicleColour(new ItemStack(Material.TNT), 5),
						new BlockVehicleColour(new ItemStack(Material.TNT), 5),
						new BlockVehicleColour(new ItemStack(Material.WOOL), 5)),
						new ItemStack(Material.TNT));
		
		/*
		m.register(new ColourButton(new ItemStack(Material.STEP,1,(byte)7),
				"White Roof", 99, Currency.STARS, Rank.DEFAULT, "vc_rwhite"), 
				new BlockVehicleColour(new ItemStack(Material.STEP,1,(byte)7),31), 
				new ItemStack(Material.STEP,1,(byte)7));
		*/
	}
}
