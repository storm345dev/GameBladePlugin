package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class UnrankedHats {
	public static void registerAll(){
		new Hat(Material.DAYLIGHT_DETECTOR, "sensorhat", 99, "Solar Hat").register();
		new Hat(Material.SPONGE, "spongehat", 99, "Sponge Hat").register();
		new Hat(Material.FENCE_GATE, "fencegatehat", 99, "Fence Gate Hat").register();
		new Hat(Material.VINE, "vinehat", 99, "Vine Hat").register();
		new Hat(Material.FENCE, "fencehat", 99, "Fence Hat").register();
		new Hat(Material.CACTUS, "cactushat", 99, "Cactus Hat").register();
		new Hat(Material.TRAP_DOOR, "trapdoorgatehat", 99, "Trapdoor Gate Hat").register();
	}
	
	private static class Hat extends BlockhatBase {
		private ItemStack item;
		private String id;
		private double price;
		private String name;
		
		@SuppressWarnings("unused")
		protected Hat(Material item, byte data, String id, double price, String name){
			this(new ItemStack(item, 1, data), id, price, name);
		}
		
		protected Hat(Material item, String id, double price, String name){
			this(new ItemStack(item), id, price, name);
		}
		
		protected Hat(ItemStack item, String id, double price, String name){
			this.item = item;
			this.id = id;
			this.price = price;
			this.name = name;
		}
		
		public void register(){
			CosmeticManager.registerCosmetic(this);
		}
		
		@Override
		public ItemStack getHeadWear() {
			return item;
		}

		@Override
		public String getID() {
			return id;
		}

		@Override
		public double getPrice() {
			return price;
		}

		@Override
		public String getUserFriendlyName() {
			return name;
		}
		
	}
}
