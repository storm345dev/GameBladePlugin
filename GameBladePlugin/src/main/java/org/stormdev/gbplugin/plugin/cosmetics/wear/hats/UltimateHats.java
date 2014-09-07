package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class UltimateHats {
	public static void registerAll(){
		new Hat(Material.COBBLE_WALL, "cobblewallhat", 0, "Cobble Wall Hat").register();
		new Hat(Material.ANVIL, "anvilhat", 149, "Anvil Hat (Badass)").register();
		new Hat(Material.DIAMOND_BLOCK, "diamondblockhat", 99, "Diamond Block Hat").register();
		new Hat(Material.SNOW, "snowlayerhat", 99, "Snow Layer Hat").register();
		new Hat(Material.THIN_GLASS, "glasspanehat", 99, "Glass Pane Hat").register();
		new Hat(Material.STEP, (byte) 4, "brickslabhat", 99, "Brick Slab Hat").register();
		new Hat(Material.JUKEBOX, "jukeboxhat", 99, "Jukebox Hat").register();
		new Hat(Material.ENDER_PORTAL_FRAME, "endportalframehat", 99, "End Portal Frame Hat").register();
		new Hat(Material.ENCHANTMENT_TABLE, "enchanthat", 99, "Enchanting Table Hat").register();
		new Hat(Material.ENDER_CHEST, "enderchesthat", 199, "Ender Chest Hat (Badass)").register();
		new Hat(Material.CHEST, "chesthat", 199, "Chest Hat (Half - Badass)").register();
		new Hat(Material.LEAVES, "leaveshat", 199, "Leaves Hat (Badass)").register();
		new Hat(Material.STRING, "stringhat", 49, "String Hat (Cutie)").register();
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
		
		@Override
		public Rank minimumRank(){
			return Rank.ULTIMATE;
		}
		
	}
}
