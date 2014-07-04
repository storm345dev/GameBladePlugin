package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import net.minecraft.server.v1_7_R3.Material;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.cosmetics.Currency;
import org.stormdev.gbapi.cosmetics.Rank;

public abstract class BlockhatBase implements Hat {
	
	@Override
	public CosmeticType getType() {
		return CosmeticType.HAT;
	}
	
	@Override
	public void remove(Player player){
		ItemStack h = player.getInventory().getHelmet();
		try {
			if(h.isSimilar(getHeadWear())){
				player.getInventory().setHelmet(null);
			}
		} catch (Exception e) {
			//Ew well
		}
	}

	@Override
	public boolean apply(Player player) {
		ItemStack h = player.getInventory().getHelmet();
		if(h != null && !h.getType().equals(Material.AIR)){
			player.sendMessage(ChatColor.RED+"Could not apply hat because you are already wearing a hat!");
			return false;
		}
		
		player.getInventory().setHelmet(getHeadWear());
		return true;
	}
	
	@Override
	public void justBought(Player player){
		player.sendMessage(ChatColor.GREEN+"Nice hat you've got there! Use /hat to put it on!");
	}
	
	@Override
	public Currency getCurrencyUsed(){
		return Currency.STARS;
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.DEFAULT;
	}

}
