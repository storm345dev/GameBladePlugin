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
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;
import org.stormdev.gbplugin.plugin.cosmetics.carts.ColouredVehicle.BlockChangingVehicleColour;
import org.stormdev.gbplugin.plugin.cosmetics.carts.ColouredVehicle.BlockVehicleColour;

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
					new String[]{
				ChatColor.RED+"Cost:",
				ChatColor.WHITE+""+cost+" "+currency.getName(),
				minimumRank != Rank.DEFAULT ? ChatColor.YELLOW+minimumRank.getName()+" or higher":"",
				ChatColor.RED+"Colour your vehicle:", 
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
		
		public ColourButton(ItemStack display, String color, int cost, Currency currency, Rank minimumRank, String id){
			this(display, 
					ChatColor.GOLD+VehicleColours.getCorrectName(color), 
					new String[]{
				ChatColor.RED+"Cost:",
				ChatColor.WHITE+""+cost+" "+currency.getName(),
				minimumRank != Rank.DEFAULT ? ChatColor.YELLOW+minimumRank.getName()+" or higher":"",
				ChatColor.RED+"Colour your vehicle:", 
				ChatColor.WHITE+VehicleColours.getCorrectName(color)},
				cost, currency, minimumRank, id
			);
		}

		@Override
		public void onClick(Player player) {
			player.sendMessage("Selected: "+super.getColouredTitle());
			GameBlade.plugin.cosmeticManager.purchaseCosmetic(player, this);
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
			GameBlade.plugin.cosmeticManager.getActiveCosmeticManager().setActiveCosmeticIDForType(player, CosmeticType.VEHICLE_COLOUR, id);
			return true;
		}

		@Override
		public void remove(Player player) {
			//Do nothing
		}

		@Override
		public void justBought(Player player) {
			player.sendMessage(ChatColor.BLUE+"Nice vehicle colour! To apply it to your vehicle, go into 'Manage Cosmetics'->'Vehicle Paint' and select it!");
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
	private Map<String, ColouredVehicle> cosmeticsCar = new TreeMap<String, ColouredVehicle>();
	private Map<String, ItemStack> cosmeticsStructure = new TreeMap<String, ItemStack>();
	
	@SuppressWarnings("deprecation")
	public BuyVehicleColoursMenu(){
		this.menu = new PagedMenu(this);
		
		ColourButton glass = new ColourButton(new ItemStack(Material.GLASS),
				"glass", 99, Currency.STARS, Rank.DEFAULT, "vc_glass");
		ColourButton ice = new ColourButton(new ItemStack(Material.ICE), "ice", 
				399, Currency.STARS, Rank.PREMIUM_PLUS, "vc_ice");
		toBuy.add(glass);
		cosmeticsCar.put(glass.getID(), new BlockVehicleColour(new ItemStack(Material.GLASS)));
		cosmeticsStructure.put(glass.getID(), new ItemStack(Material.GLASS));
		CosmeticManager.registerCosmetic(glass);
		
		ColourButton cop = new ColourButton(new ItemStack(Material.PAPER), "Cop (Changes colour)",
				299, Currency.STARS, Rank.PREMIUM, "vc_cop");	
		toBuy.add(cop);
		cosmeticsCar.put(cop.getID(), new BlockChangingVehicleColour(
				20l, new BlockVehicleColour(new ItemStack(Material.WOOL, 1, (byte) 11), 5), //Blue stained glass
				new BlockVehicleColour(new ItemStack(Material.WOOL, 1, (byte) 14), 5) //Red stained glass
				));
		cosmeticsStructure.put(cop.getID(), new ItemStack(Material.STAINED_GLASS));
		CosmeticManager.registerCosmetic(cop);
		
		ColourButton funky = new ColourButton(new ItemStack(Material.PAPER), "Funky (Changes colour & Height)",
				999, Currency.STARS, Rank.PREMIUM_PLUS, "vc_funky");	
		toBuy.add(funky);
		cosmeticsCar.put(funky.getID(), new BlockChangingVehicleColour(
				5l, getFunky() //Red stained glass
				));
		cosmeticsStructure.put(funky.getID(), new ItemStack(Material.STAINED_GLASS));
		CosmeticManager.registerCosmetic(funky);
		
		ColourButton rainbow = new ColourButton(new ItemStack(Material.PAPER), "Rainbow (Changes colour)",
				499, Currency.STARS, Rank.PREMIUM_PLUS, "vc_rainbow");
		toBuy.add(rainbow);
		cosmeticsCar.put(rainbow.getID(), new BlockChangingVehicleColour(
				20l, getAllStainedGlass()));
		cosmeticsStructure.put(rainbow.getID(), new ItemStack(Material.STAINED_GLASS));
		CosmeticManager.registerCosmetic(rainbow);
		
		toBuy.add(ice);
		cosmeticsCar.put(ice.getID(), new BlockVehicleColour(new ItemStack(Material.ICE), 5));
		cosmeticsStructure.put(ice.getID(), new ItemStack(Material.ICE));
		CosmeticManager.registerCosmetic(ice);
		
		VehicleCosmeticRegistry.registerAll(this);
	}
	
	public void register(ColourButton but, ColouredVehicle vehCol, ItemStack structure){
		toBuy.add(but);
		cosmeticsCar.put(but.getID(), vehCol);
		cosmeticsStructure.put(but.getID(), structure);
		CosmeticManager.registerCosmetic(but);
	}
	
	public BlockVehicleColour[] getAllStainedGlass(){
		List<BlockVehicleColour> list = new ArrayList<BlockVehicleColour>();
		
		for(int i=0;i<16;i++){ //From 0 to 15
			list.add(new BlockVehicleColour(new ItemStack(Material.STAINED_CLAY, 1, (byte) i), 5));
		}
		
		return list.toArray(new BlockVehicleColour[]{});
	}
	
	public BlockVehicleColour[] getFunky(){
		List<BlockVehicleColour> list = new ArrayList<BlockVehicleColour>();
		
		int z = 10;
		for(int i=0;i<16;i++){ //From 0 to 15
			list.add(new BlockVehicleColour(new ItemStack(Material.STAINED_CLAY, 1, (byte) i), z));
			z++;
		}
		
		for(int i=0;i<16;i++){ //From 0 to 15
			list.add(new BlockVehicleColour(new ItemStack(Material.STAINED_CLAY, 1, (byte) i), z));
			z--;
		}
		
		return list.toArray(new BlockVehicleColour[]{});
	}
	
	public ColouredVehicle getCarCosmeticBlock(String id){
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
