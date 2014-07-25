package org.stormdev.gbplugin.plugin.cosmetics.carts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.cosmetics.VehicleColours;
import org.stormdev.gbapi.gui.PagedMenu;
import org.stormdev.gbapi.gui.PagedMenu.MenuDetails;
import org.stormdev.gbapi.storm.misc.Sch;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.carts.BuyVehicleColoursMenu.ColourButton;

public class VehicleColoursMenu implements MenuDetails {
	
	public static class ColouredButton extends AbstractMenuItem {

		private String cosmoId = "";
		private boolean blank = false;
		
		public ColouredButton(DyeColor color, String cosmeticId){
			this(VehicleColours.getItemStack(color), 
					ChatColor.GOLD+VehicleColours.getCorrectName(color.name()), 
					cosmeticId,
					new String[]{ChatColor.RED+"Colour your vehicle:", 
				ChatColor.WHITE+VehicleColours.getCorrectName(color.name())});
		}
		
		public ColouredButton(ItemStack display, String colouredTitle, String cosmeticId,
				String... lore) {
			super(display, colouredTitle, lore);
			this.cosmoId = cosmeticId;
			if(ChatColor.stripColor(colouredTitle).equalsIgnoreCase("clear")){
				blank = true;
			}
		}
		
		public String getCosmeticID(){
			return cosmoId;
		}
		
		public boolean isClear(){
			return blank;
		}

		@Override
		public void onClick(final Player player) {
			player.sendMessage("Set vehicle colour to "+super.getColouredTitle());
			Sch.runAsync(new Runnable(){

				@Override
				public void run() {
					String id = isClear() ? null:getCosmeticID();
					GameBlade.plugin.cosmeticManager.getActiveCosmeticManager().setActiveCosmeticIDForType(player, CosmeticType.VEHICLE_COLOUR, id);
					return;
				}});
		}
		
	}
	
	private PagedMenu menu;
	
	
	private static VehicleColoursMenu instance = null;
	public static VehicleColoursMenu getInstance(){
		if(instance == null){
			instance = new VehicleColoursMenu();
		}
		return instance;
	}
	
	public VehicleColoursMenu(){
		this.menu = new PagedMenu(this);
	}
	
	public PagedMenu getMenu(){
		return menu;
	}

	@Override
	public List<MenuItem> getDisplayItems(Player player) {
		List<MenuItem> owned =  new ArrayList<MenuItem>(Arrays.asList(new MenuItem[]{
				new ColouredButton(new ItemStack(Material.THIN_GLASS), ChatColor.GOLD+"Clear", "", ChatColor.RED+"Don't colour your vehicle")
		}));
		
		List<Cosmetic> cosmetics = GameBlade.plugin.cosmeticManager.getOwnedByType(player, CosmeticType.VEHICLE_COLOUR);
		for(Cosmetic c:cosmetics){
			if(c instanceof ColourButton){
				ColourButton cb = (ColourButton) c;
				ItemStack display = cb.getDisplayItem().clone();
				ColouredButton b = new ColouredButton(display, 
						ChatColor.GOLD+cb.getColouredTitle(), 
						cb.getID(),
						ChatColor.RED+"Set your vehicle colour to:", 
						ChatColor.WHITE+ChatColor.stripColor(cb.getColouredTitle()));
				owned.add(b);
			}
		}
		return owned;
	}

	@Override
	public String getColouredMenuTitle(Player player) {
		return ChatColor.YELLOW+"My Vehicle Colour";
	}

	@Override
	public int getPageSize() {
		return 18;
	}

	@Override
	public String noDisplayItemMessage() {
		return "You don't own any custom cart colours!";
	}
}
