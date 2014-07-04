package org.stormdev.gbplugin.cosmetics.cosmetics.hats;

import net.minecraft.server.v1_7_R3.Material;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.cosmetics.Currency;

public abstract class BlockhatBase implements Cosmetic,Hat {
	
	@Override
	public CosmeticType getType() {
		return CosmeticType.HAT;
	}
	
	@Override
	public void remove(Player player){
		ItemStack h = player.getInventory().getHelmet();
		if(h.isSimilar(getHeadWear())){
			player.getInventory().setHelmet(null);
		}
	}

	@Override
	public void apply(Player player) {
		ItemStack h = player.getInventory().getHelmet();
		if(h != null && !h.getType().equals(Material.AIR)){
			player.sendMessage(ChatColor.RED+"You are already wearing a hat!");
			return;
		}
		
		player.getInventory().setHelmet(getHeadWear());
	}
	
	@Override
	public void justBought(Player player){
		player.sendMessage(ChatColor.GREEN+"Nice hat you've got there!");
	}
	
	@Override
	public Currency getCurrencyUsed(){
		return Currency.STARS;
	}

}
