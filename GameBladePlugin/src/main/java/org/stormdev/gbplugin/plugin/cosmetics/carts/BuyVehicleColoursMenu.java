package org.stormdev.gbplugin.plugin.cosmetics.carts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.cosmetics.Currency;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.cosmetics.VehicleColours;
import org.stormdev.gbapi.gui.PagedMenu;
import org.stormdev.gbapi.gui.PagedMenu.MenuDetails;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class BuyVehicleColoursMenu implements MenuDetails {
	
	public static class ColourButton extends AbstractMenuItem implements Cosmetic {
		
		private int cost = 0;
		private Currency currency = Currency.STARS;
		private Rank minimumRank = Rank.DEFAULT;
		private String name;
		private String id;
		
		public ColourButton(DyeColor color, int cost, Currency currency, Rank minimumRank, String id){
			this(VehicleColours.getItemStack(color), 
					ChatColor.GOLD+VehicleColours.getCorrectName(color.name()), 
					new String[]{ChatColor.RED+"Colour your vehicle:", 
				ChatColor.WHITE+VehicleColours.getCorrectName(color.name())},
				cost, currency, minimumRank, id
			);
		}
		
		public ColourButton(ItemStack display, String colouredTitle,
				String[] lore, int cost, Currency currency, Rank minimumRank, String id) {
			super(display, colouredTitle, lore);
			this.cost = cost;
			this.currency = currency;
			this.minimumRank = minimumRank;
			this.name = ChatColor.stripColor(colouredTitle);
			this.id = id;
		}

		@Override
		public void onClick(Player player) {
			player.sendMessage("You clicked "+super.getColouredTitle());
			//TODO ACTUALLY do something useful
		}

		@Override
		public String getID() {
			return id;
		}

		@Override
		public CosmeticType getType() {
			return CosmeticType.VEHICLE_COLOUR;
		}

		@Override
		public boolean apply(Player player) {
			//TODO Set as their active vehicle colour
			return true;
		}

		@Override
		public void remove(Player player) {
			//Do nothing
		}

		@Override
		public void justBought(Player player) {
			// TODO 
		}

		@Override
		public double getPrice() {
			return cost;
		}

		@Override
		public String getUserFriendlyName() {
			return name;
		}

		@Override
		public Currency getCurrencyUsed() {
			return currency;
		}

		@Override
		public Rank minimumRank() {
			return minimumRank;
		}
		
	}
	
	private PagedMenu menu;
	
	
	private static BuyVehicleColoursMenu instance = null;
	public static BuyVehicleColoursMenu getInstance(){
		if(instance == null){
			instance = new BuyVehicleColoursMenu();
		}
		return instance;
	}
	
	
	private List<MenuItem> toBuy = new ArrayList<MenuItem>();
	private Map<String, ItemStack> cosmeticsCar = new TreeMap<String, ItemStack>();
	private Map<String, ItemStack> cosmeticsStructure = new TreeMap<String, ItemStack>();
	
	@SuppressWarnings("deprecation")
	public BuyVehicleColoursMenu(){
		this.menu = new PagedMenu(this);
		
		ColourButton glass = new ColourButton(new ItemStack(Material.GLASS), ChatColor.GOLD+"Glass", 
				new String[]{ChatColor.RED+"Set your vehicle colour to clear glass"}, 99, Currency.STARS, Rank.DEFAULT, "vc_glass");
		ColourButton ice = new ColourButton(new ItemStack(Material.ICE), ChatColor.GOLD+"Ice", 
				new String[]{ChatColor.RED+"Set your vehicle colour to ice"}, 99, Currency.STARS, Rank.DEFAULT, "vc_ice");
		toBuy.add(glass);
		cosmeticsCar.put(glass.getID(), new ItemStack(Material.GLASS));
		cosmeticsStructure.put(glass.getID(), new ItemStack(Material.GLASS));
		CosmeticManager.registerCosmetic(glass);
		
		ColourButton cop = new ColourButton(new ItemStack(Material.PAPER), ChatColor.GOLD+"Paper", 
				new String[]{ChatColor.RED+"Set your vehicle colour to flash like a cop car"}, 299, Currency.STARS, Rank.PREMIUM, "vc_cop"); //TODO Actually make work		
		toBuy.add(cop);
		cosmeticsCar.put(cop.getID(), new ItemStack(Material.STAINED_GLASS));
		cosmeticsStructure.put(cop.getID(), new ItemStack(Material.STAINED_GLASS));
		CosmeticManager.registerCosmetic(cop);
		
		toBuy.add(ice);
		cosmeticsCar.put(ice.getID(), new ItemStack(Material.ICE));
		cosmeticsStructure.put(ice.getID(), new ItemStack(Material.ICE));
		CosmeticManager.registerCosmetic(ice);
		
		for(DyeColor color:DyeColor.values()){
			ColourButton but = new ColourButton(color, 199, Currency.STARS, Rank.VIP, "vc_"+color.name().toLowerCase());
			toBuy.add(but);
			
			Wool w = new Wool();
			w.setColor(color);
			ItemStack glas = new ItemStack(Material.STAINED_GLASS, 1, w.getData());
			
			cosmeticsCar.put(but.getID(), glas);
			cosmeticsStructure.put(but.getID(), new ItemStack(Material.STAINED_CLAY, 1, w.getData()));
			CosmeticManager.registerCosmetic(but);
		}
	}
	
	public ItemStack getCarCosmeticBlock(String id){
		return cosmeticsCar.get(id);
	}
	
	public ItemStack getStructureCosmeticBlock(String id){
		return cosmeticsStructure.get(id);
	}
	
	public List<String> getCosmeticIDs(){
		return new ArrayList<String>(cosmeticsCar.keySet());
	}
	
	public PagedMenu getMenu(){
		return menu;
	}

	@Override
	public List<MenuItem> getDisplayItems(Player player) {
		return toBuy;
	}

	@Override
	public String getColouredMenuTitle(Player player) {
		return ChatColor.YELLOW+"Buy Vehicle Colours";
	}

	@Override
	public int getPageSize() {
		return 18;
	}

	@Override
	public String noDisplayItemMessage() {
		return "No vehicle colours available!";
	}
}
