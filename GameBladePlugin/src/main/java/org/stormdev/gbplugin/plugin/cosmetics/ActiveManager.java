package org.stormdev.gbplugin.plugin.cosmetics;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbapi.storm.misc.Sch;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.carts.BuyVehicleColoursMenu;
import org.stormdev.gbplugin.plugin.cosmetics.carts.ColouredVehicle;

public class ActiveManager implements org.stormdev.gbapi.cosmetics.ActiveCosmeticManager {

	private static Map<CosmeticType, String> SQL_TABLES = new HashMap<CosmeticType, String>();
	private static Map<CosmeticType, String> SQL_COLUMNS = new HashMap<CosmeticType, String>();
	private static final String SQL_ACTIVE_TABLE = "activeCosmetics";
	private static final String SQL_HATS_TABLE = CosmeticManager.SQL_TABLE;
	private static final String SQL_HATS_COLUMN = "hat";
	private static final String SQL_VEHICLE_COLOUR_COLUMN = "vehicleColour";
	private static final String SQL_ID_KEY = CosmeticManager.SQL_ID_KEY;
	
	private static final String[] SQL_ACTIVE_TABLE_COLUMNS = new String[]{SQL_ID_KEY, SQL_VEHICLE_COLOUR_COLUMN};
	private static final String[] SQL_ACTIVE_TABLE_VALUES = new String[]{"varchar(255) NOT NULL PRIMARY KEY", "varchar(255)"};
	
	static {
		SQL_TABLES.put(CosmeticType.HAT, SQL_HATS_TABLE);
		for(CosmeticType t:CosmeticType.values()){
			if(!SQL_TABLES.containsKey(t)){
				SQL_TABLES.put(t, SQL_ACTIVE_TABLE);
			}
		}
		
		SQL_COLUMNS.put(CosmeticType.HAT, SQL_HATS_COLUMN);
		SQL_COLUMNS.put(CosmeticType.VEHICLE_COLOUR, SQL_VEHICLE_COLOUR_COLUMN);
	}
	
	public static String getTable(CosmeticType type){
		if(SQL_TABLES.containsKey(type)){
			return SQL_TABLES.get(type);
		}
		return SQL_ACTIVE_TABLE;
	}
	
	public static String getColumn(CosmeticType type){
		if(SQL_COLUMNS.containsKey(type)){
			return SQL_COLUMNS.get(type);
		}
		return null; //Uh oh, type doesn't have an active thingy in the sql
	}
	
	public ActiveManager(){
		GameBlade.plugin.GBSQL.createTable(SQL_ACTIVE_TABLE, SQL_ACTIVE_TABLE_COLUMNS, SQL_ACTIVE_TABLE_VALUES);
	}
	
	@Override
	public String getActiveCosmeticIDForType(CosmeticType type, Player player) {
		Sch.notSync();
		return getActiveCosmeticIDForType(type, PlayerIDFinder.getMojangID(player).getID());
	}

	@Override
	public String getActiveCosmeticIDForType(CosmeticType type, String uuid) {
		Sch.notSync();
		try {
			String active = GameBlade.plugin.GBSQL.searchTable(getTable(type), SQL_ID_KEY, uuid, getColumn(type)).toString();
			if(active != null && !active.equalsIgnoreCase("null") && active.length() > 0){
				return active;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			return null;
		}
		
		return null;
	}

	@Override
	public void setActiveCosmeticIDForType(final Player player, final CosmeticType type,
			final String id) {
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				setActiveCosmeticIDForType(PlayerIDFinder.getMojangID(player).getID(), type, id);
				return;
			}});
	}

	@Override
	public void setActiveCosmeticIDForType(final String uuid, final CosmeticType type,
			final String id) {
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				boolean clearIt = id == null || id.length() < 1;
				
				try {
					if(clearIt){
						GameBlade.plugin.GBSQL.setInTable(getTable(type), SQL_ID_KEY, uuid, getColumn(type), null);
						return;
					}
					
					GameBlade.plugin.GBSQL.setInTable(getTable(type), SQL_ID_KEY, uuid, getColumn(type), id);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}});
	}

	@Override
	public void fillMinecartVehicle(final Minecart cart, final Player player) {
		Sch.runAsync(new Runnable(){

			@Override
			public void run() {
				String active = getActiveCosmeticIDForType(CosmeticType.VEHICLE_COLOUR, player);
				if(active == null){ //None active
					return;
				}
				
				ColouredVehicle handler = BuyVehicleColoursMenu.getInstance().getCarCosmeticBlock(active);
				if(handler == null){
					return;
				}
				
				handler.apply(cart, player);
				cart.removeMetadata("vehicle.fillHandler", GameBlade.plugin);
				cart.setMetadata("vehicle.fillHandler", new MetadataValue(handler, GameBlade.plugin));
				return;
			}});
		return;
	}

}
