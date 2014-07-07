package org.stormdev.gbplugin.bans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.stormdev.gbapi.bans.BanHandler.Time;
import org.stormdev.gbapi.bans.PunishmentLogs.PunishmentLog;
import org.stormdev.gbapi.bans.PunishmentLogs.PunishmentType;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.bans.ChatInput.InputValidator;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.utils.MetaValue;

public class BanPanel {
	private String playerName;
	private static IconMenu selectMode;
	public static final String meta = "gameblade.banPanel";
	
	static {
		selectMode = new IconMenu("Select Mode", 9, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				int slot = event.getPosition();
				Mode mode = null;
				if(slot == 0){
					mode = Mode.BAN;
				}
				else if(slot == 1){
					mode = Mode.PROFILE;
				}
				else {
					event.setWillClose(false);
					event.setWillDestroy(false);
					return;
				}
				
				BanPanel bp = get(event.getPlayer());
				if(bp != null){
					bp.selected(event.getPlayer(), mode);
				}
				
				event.setWillClose(true);
				event.setWillDestroy(false);
				return;
			}}, GameBlade.plugin);
		
		selectMode.setOption(0, new ItemStack(Material.STICK), ChatColor.GOLD+"Ban", ChatColor.RED+"Ban somebody");
		selectMode.setOption(1, new ItemStack(Material.PAPER), ChatColor.GOLD+"Profile", ChatColor.RED+"View a players' profile");
	}
	
	enum Mode {
		BAN, PROFILE;
	}
	
	public static BanPanel get(Player player){
		if(player.hasMetadata(BanPanel.meta)){
			Object o = player.getMetadata(BanPanel.meta).get(0).value();
			player.removeMetadata(BanPanel.meta, GameBlade.plugin);
			
			if(o instanceof BanPanel){
				return (BanPanel) o;
			}
		}
		
		return null;
	}
	
	private Mode mode = Mode.BAN;
	private String otherPlayer = null;
	private IconMenu current = null;
	private Time time = null;
	private String reason = null;
	
	public BanPanel(Player player){
		this.playerName = player.getName();
		player.setMetadata(meta, new MetaValue(this, GameBlade.plugin));
		selectMode.open(player);
	}
	
	private void selected(Player player, final Mode mode){
		this.mode = mode;
		new ChatInput(player, new InputValidator(){

			@Override
			public String getHelpMessage() {
				return ChatColor.GREEN+"Enter the name of the player into the chat (CaSe SeNsItIvE)";
			}

			@Override
			public boolean isValid(Player player, String input) {
				if(input.length() > 16 || input.length() < 1){
					return false;
				}
				if(input.contains(" ")){
					return false;
				}
				return true;
			}

			@Override
			public void onValidInput(Player player, String input) {
				onPlayerSelected(player, input);
			}

			@Override
			public String getInvalidMessage() {
				return ChatColor.RED+"Invalid player name!";
			}});
	}
	
	public void onPlayerSelected(Player admin, String name){
		this.otherPlayer = name;
		admin.sendMessage(ChatColor.GRAY+"Received: '"+name+"'");
		if(mode.equals(Mode.BAN)){
			openBanMenu(admin);
		}
		else {
			openProfileMenu(admin);
		}
	}
	
	public void openBanMenu(Player admin){
		openDurationMenu(admin);
	}
	
	public void onReasonSelect(final Player player, final String reason){
		this.reason = reason;
		player.sendMessage(ChatColor.GRAY+"Reason: "+reason);
		
		Player toBan = Bukkit.getPlayer(otherPlayer);
		if(toBan != null){
			GameBlade.api.getBans().ban(toBan, player, reason, time);
		}
		else {
			Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					GameBlade.api.getBans().ban(PlayerIDFinder.getMojangID(otherPlayer).getID(), player, reason, time);
					return;
				}});
		}
		
		String duration = time.isForever() ? "forever":((int) (time.getDuration()/1000/60/60))+" hours";
		player.sendMessage(ChatColor.GREEN+"Banned!");
		
		for(Player p:Bukkit.getOnlinePlayers()){
			if(p.hasPermission("gameblade.admin")){
				p.sendMessage(ChatColor.YELLOW+player.getName()+" has banned "+otherPlayer+" for "+reason+" for "+duration+"!");
			}
		}
		GameBlade.logger.info(ChatColor.YELLOW+player.getName()+" has banned "+otherPlayer+" for "+reason+" for "+duration+"!");
	}
	
	public void onTimeSelect(final Player player, Time time){
		this.time = time;
		player.sendMessage(ChatColor.GRAY+"Selected time!");
		//Select the reason
		Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				openReasonMenu(player);
				return;
			}}, 2l);
	}
	
	public void openReasonMenu(Player admin){
		if(admin == null){
			return;
		}
		meta(admin);
		
		current = new IconMenu("Select Reason", 18, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				int pos = event.getPosition();
				String reason = "-";
				if(time.isForever()){
					if(pos == 0){	reason = "Using a hacked client";	}
					else if(pos == 1){	reason = "Advertising";	}
					else if(pos == 2){	reason = "Using a banned client-mod";	}
					else if(pos == 3){	reason = "Racism";	}
					else if(pos == 4){	reason = "Sexism";	}
					else if(pos == 5){	reason = "Raiding";	}
					else if(pos == 6){	reason = "Abusing bugs";	}
					else if(pos == 7){	reason = "Disrespecting players";	}
					else if(pos == 8){	reason = "Disrespecting staff";	}
					else if(pos == 9){
						customReasonInput(event.getPlayer());
						event.setWillClose(true);
						event.setWillDestroy(true);
						return;
					}
					else {
						event.setWillClose(false);
						event.setWillDestroy(false);
					}
				}
				else {
					if(pos == 0){	reason = "Spamming";	}
					else if(pos == 1){	reason = "Advertising";	}
					else if(pos == 2){	reason = "Using caps-lock";	}
					else if(pos == 3){	reason = "Swearing";	}
					else if(pos == 4){	reason = "House camping";	}
					else if(pos == 5){	reason = "Acting as a staff member";	}
					else if(pos == 6){	reason = "Spreading lies";	}
					else if(pos == 7){
						customReasonInput(event.getPlayer());
						event.setWillClose(true);
						event.setWillDestroy(true);
						return;
					}
					else {
						event.setWillClose(false);
						event.setWillDestroy(false);
					}
				}
				onReasonSelect(event.getPlayer(), reason);
				event.setWillClose(true);
				event.setWillDestroy(true);
				return;
			}}, GameBlade.plugin);
		if(time.isForever()){
			current.setOption(0, new ItemStack(Material.PAPER), ChatColor.GOLD+"Using a hacked client", ChatColor.RED+"Ban for this reason");
			current.setOption(1, new ItemStack(Material.PAPER), ChatColor.GOLD+"Advertising", ChatColor.RED+"Ban for this reason");
			current.setOption(2, new ItemStack(Material.PAPER), ChatColor.GOLD+"Using a banned client-mod", ChatColor.RED+"Ban for this reason");
			current.setOption(3, new ItemStack(Material.PAPER), ChatColor.GOLD+"Racism", ChatColor.RED+"Ban for this reason");
			current.setOption(4, new ItemStack(Material.PAPER), ChatColor.GOLD+"Sexism", ChatColor.RED+"Ban for this reason");
			current.setOption(5, new ItemStack(Material.PAPER), ChatColor.GOLD+"Raiding", ChatColor.RED+"Ban for this reason");
			current.setOption(6, new ItemStack(Material.PAPER), ChatColor.GOLD+"Abusing bugs", ChatColor.RED+"Ban for this reason");
			current.setOption(7, new ItemStack(Material.PAPER), ChatColor.GOLD+"Disrespecting players", ChatColor.RED+"Ban for this reason");
			current.setOption(8, new ItemStack(Material.PAPER), ChatColor.GOLD+"Disrespecting staff", ChatColor.RED+"Ban for this reason");
			current.setOption(9, new ItemStack(Material.PAPER), ChatColor.GOLD+"Custom (Specify)", ChatColor.RED+"A custom reason");
		}
		else {
			current.setOption(0, new ItemStack(Material.PAPER), ChatColor.GOLD+"Spamming", ChatColor.RED+"Ban for this reason");
			current.setOption(1, new ItemStack(Material.PAPER), ChatColor.GOLD+"Advertising", ChatColor.RED+"Ban for this reason");
			current.setOption(2, new ItemStack(Material.PAPER), ChatColor.GOLD+"Using caps-lock", ChatColor.RED+"Ban for this reason");
			current.setOption(3, new ItemStack(Material.PAPER), ChatColor.GOLD+"Swearing", ChatColor.RED+"Ban for this reason");
			current.setOption(4, new ItemStack(Material.PAPER), ChatColor.GOLD+"House camping", ChatColor.RED+"Ban for this reason");
			current.setOption(5, new ItemStack(Material.PAPER), ChatColor.GOLD+"Acting as a staff member", ChatColor.RED+"Ban for this reason");
			current.setOption(6, new ItemStack(Material.PAPER), ChatColor.GOLD+"Spreading lies", ChatColor.RED+"Ban for this reason");
			current.setOption(7, new ItemStack(Material.PAPER), ChatColor.GOLD+"Custom (Specify)", ChatColor.RED+"A custom reason");
		}
		
		current.open(admin);
	}
	
	public void openDurationMenu(Player admin){
		meta(admin);
		current = new IconMenu("Select Duration", 9, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				int pos = event.getPosition();
				Time time = null;
				if(pos == 0){
					time = BanTime.HOUR.getNewTime();
				}
				else if(pos == 1){
					time = BanTime.DAY.getNewTime();
				}
				else if(pos == 2){
					time = BanTime.WEEK.getNewTime();
				}
				else if(pos == 3){
					time = BanTime.MONTH.getNewTime();
				}
				else if(pos == 4){
					time = BanTime.THREE_MONTHS.getNewTime();
				}
				else if(pos == 5){
					time = BanTime.SIX_MONTHS.getNewTime();
				}
				else if(pos == 6){
					time = BanTime.YEAR.getNewTime();
				}
				else if(pos == 7){
					time = BanTime.FOREVER.getNewTime();
				}
				else if(pos == 8){
					customTimeInput(event.getPlayer());
					event.setWillClose(true);
					event.setWillDestroy(true);
					return;
				}
				else {
					event.setWillClose(false);
					event.setWillDestroy(false);
					return;
				}
				
				onTimeSelect(event.getPlayer(), time);
				
				event.setWillClose(true);
				event.setWillDestroy(true);
				return;
			}}, GameBlade.plugin);
		current.setOption(0, new ItemStack(Material.PAPER), ChatColor.GOLD+"1 hour", ChatColor.RED+"Ban for this amount of time");
		current.setOption(1, new ItemStack(Material.PAPER), ChatColor.GOLD+"1 day", ChatColor.RED+"Ban for this amount of time");
		current.setOption(2, new ItemStack(Material.PAPER), ChatColor.GOLD+"1 week", ChatColor.RED+"Ban for this amount of time");
		current.setOption(3, new ItemStack(Material.PAPER), ChatColor.GOLD+"1 month", ChatColor.RED+"Ban for this amount of time");
		current.setOption(4, new ItemStack(Material.PAPER), ChatColor.GOLD+"3 months", ChatColor.RED+"Ban for this amount of time");
		current.setOption(5, new ItemStack(Material.PAPER), ChatColor.GOLD+"6 months", ChatColor.RED+"Ban for this amount of time");
		current.setOption(6, new ItemStack(Material.PAPER), ChatColor.GOLD+"12 months", ChatColor.RED+"Ban for this amount of time");
		current.setOption(7, new ItemStack(Material.PAPER), ChatColor.GOLD+"forever", ChatColor.RED+"Ban for this amount of time");
		current.setOption(8, new ItemStack(Material.PAPER), ChatColor.GOLD+"Custom hours", ChatColor.RED+"Set a no. of hours");
		
		current.open(admin);
	}
	
	public void openProfileMenu(final Player admin){
		admin.sendMessage(ChatColor.GRAY+"Loading...");
		destroy();
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				final String uuid = PlayerIDFinder.getMojangID(otherPlayer).getID();
				boolean banned = GameBlade.api.getBans().isBanned(uuid);
				int bans = 0;
				try {
					Object o = GameBlade.plugin.GBSQL.searchTable("playerdata", "id", uuid, "bans");
					if(o != null){
						try {
							bans = Integer.parseInt(o.toString());
						} catch (NumberFormatException e) {
							//Not been banned before
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				String reason = null;
				String whoBanned = null;
				String timeLeft = null;
				if(banned){
					reason = GameBlade.api.getBans().getBanReason(uuid);
					whoBanned = GameBlade.api.getBans().getWhoBanned(uuid);
					timeLeft = GameBlade.api.getBans().getBanDuration(uuid).getRemainingTime();
				}
				
				current = new IconMenu("Profile of "+otherPlayer, 9, new OptionClickEventHandler(){

					@Override
					public void onOptionClick(OptionClickEvent event) {
						int slot = event.getPosition();
						final Player player = event.getPlayer();
						if(slot == 1){
							//Open past bans
							Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

								@Override
								public void run() {
									showPast(PunishmentType.BAN, player, uuid);
									return;
								}}, 2l);
						}
						else if(slot == 2){
							//Open past kicks
							Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

								@Override
								public void run() {
									showPast(PunishmentType.KICK, player, uuid);
									return;
								}}, 2l);
						}
						else if(slot == 3){
							//Open past warns
							Bukkit.getScheduler().runTaskLaterAsynchronously(GameBlade.plugin, new Runnable(){

								@Override
								public void run() {
									showPast(PunishmentType.WARN, player, uuid);
									return;
								}}, 2l);
						}
						
						event.setWillDestroy(true);
						event.setWillClose(true);
						return;
					}}, GameBlade.plugin);
				
				ItemStack profile = new ItemStack(Material.SKULL_ITEM, 1);
				SkullMeta skullMeta = (SkullMeta) profile.getItemMeta();
				profile.setDurability((short) 3);
				skullMeta.setDisplayName(ChatColor.RED+"Profile of "+ChatColor.GOLD+otherPlayer+ChatColor.RED+":");
				skullMeta.setOwner(otherPlayer);
				profile.setItemMeta(skullMeta);
				
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GOLD+"UUID: "+ChatColor.WHITE+uuid);
				lore.add(ChatColor.GOLD+"Bans: "+ChatColor.WHITE+bans);
				lore.add(ChatColor.GOLD+"Currently Banned: "+ChatColor.WHITE+banned);
				if(banned){
					lore.add(ChatColor.GOLD+"Ban Reason: "+ChatColor.WHITE+reason);
					lore.add(ChatColor.GOLD+"Banned by: "+ChatColor.WHITE+whoBanned);
					lore.add(ChatColor.GOLD+"Ban time left: "+ChatColor.WHITE+timeLeft);
				}
				
				current.setOption(0, profile, ChatColor.RED+"Profile of "+ChatColor.GOLD+otherPlayer+ChatColor.RED+":", 
						lore);
				current.setOption(1, new ItemStack(Material.PAPER), ChatColor.RED+"View previous bans", ChatColor.GOLD+"View this players' previous bans");
				current.setOption(2, new ItemStack(Material.PAPER), ChatColor.RED+"View previous kicks", ChatColor.GOLD+"View this players' previous kicks");
				current.setOption(3, new ItemStack(Material.PAPER), ChatColor.RED+"View previous warns", ChatColor.GOLD+"View this players' previous warns");
				
				current.open(admin);
				
				/*
				admin.sendMessage(ChatColor.RED+"Profile of "+ChatColor.GOLD+otherPlayer+ChatColor.RED+":");
				admin.sendMessage(ChatColor.GOLD+"UUID: "+ChatColor.WHITE+uuid);
				admin.sendMessage(ChatColor.GOLD+"Bans: "+ChatColor.WHITE+bans);
				admin.sendMessage(ChatColor.GOLD+"Currently Banned: "+ChatColor.WHITE+banned);
				if(banned){
					admin.sendMessage(ChatColor.GOLD+"Ban Reason: "+ChatColor.WHITE+reason);
					admin.sendMessage(ChatColor.GOLD+"Banned by: "+ChatColor.WHITE+whoBanned);
					admin.sendMessage(ChatColor.GOLD+"Ban time left: "+ChatColor.WHITE+timeLeft);
				}
				*/
				
				return;
			}});
	}
	
	private void showPast(final PunishmentType type, final Player admin, final String punished_uuid){
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				admin.sendMessage(ChatColor.RED+"Past "+type.getUserFriendlyPlural()+":");
				List<PunishmentLog> past = GameBlade.api.getPunishmentLogger().getLogs(punished_uuid, type);
				for(PunishmentLog log:past){
					admin.sendMessage(ChatColor.GOLD+log.toUserFriendlyString());
				}
				return;
			}});
		
	}
	
	public void destroy(){
		if(current != null){
			current.destroy();
		}
		Player player = Bukkit.getPlayer(playerName);
		if(player != null){
			player.removeMetadata(BanPanel.meta, GameBlade.plugin);
		}
	}
	
	public void onClose(){
		destroy();
	}
	
	private void customTimeInput(Player player){
		new ChatInput(player, new InputValidator(){

			@Override
			public String getHelpMessage() {
				return ChatColor.GREEN+"Enter the time (In hours) to ban for (In the chat):";
			}

			@Override
			public boolean isValid(Player player, String input) {
				try {
					Double.parseDouble(input);
				} catch (NumberFormatException e) {
					return false;
				}
				return true;
			}

			@Override
			public void onValidInput(Player player, String input) {
				double d = Double.parseDouble(input);
				long ms = (long) (d*Times.hour);
				onTimeSelect(player, new Time(ms));
			}

			@Override
			public String getInvalidMessage() {
				return ChatColor.RED+"Not a valid number of hours!";
			}});
	}
	
	public void customReasonInput(Player player){
		new ChatInput(player, new InputValidator(){

			@Override
			public String getHelpMessage() {
				return ChatColor.GREEN+"Enter the reason to ban for (In the chat):";
			}

			@Override
			public boolean isValid(Player player, String input) {
				return true;
			}

			@Override
			public void onValidInput(Player player, String input) {
				onReasonSelect(player, input);
			}

			@Override
			public String getInvalidMessage() {
				return ChatColor.RED+"Invalid reason?";
			}});
	}
	
	private void meta(Player player){
		if(!player.hasMetadata(meta)){
			player.setMetadata(meta, new MetaValue(this, GameBlade.plugin));
		}
	}
}
