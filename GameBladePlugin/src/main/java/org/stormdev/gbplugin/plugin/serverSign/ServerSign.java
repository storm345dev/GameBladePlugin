package org.stormdev.gbplugin.plugin.serverSign;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.stormdev.gbapi.storm.misc.SerializableLocation;

public class ServerSign implements Serializable {
	private static final long serialVersionUID = 1L;

	private SerializableLocation sloc;
	private transient Location loc;
	private String displayName;
	private String bungeeServerName;
	private String mineManagerServerName;
	private String ipNoPort;
	private int port;
	
	public ServerSign(Location loc, String displayName, String bungeeName, String mmName, String ip, int port){
		this.sloc = new SerializableLocation(loc);
		this.loc = loc;
		this.displayName = displayName;
		this.bungeeServerName = bungeeName;
		this.mineManagerServerName = mmName;
		this.ipNoPort = ip;
		this.port = port;
	}

	public Location getLoc() {
		if(this.loc == null){
			this.loc = getSloc().getLocation(Bukkit.getServer());
		}
		return this.loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
		this.sloc = new SerializableLocation(loc);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getBungeeServerName() {
		return bungeeServerName;
	}

	public void setBungeeServerName(String bungeeServerName) {
		this.bungeeServerName = bungeeServerName;
	}

	public String getMineManagerServerName() {
		return mineManagerServerName;
	}

	public void setMineManagerServerName(String mineManagerServerName) {
		this.mineManagerServerName = mineManagerServerName;
	}

	public String getIpNoPort() {
		return ipNoPort;
	}

	public void setIpNoPort(String ipNoPort) {
		this.ipNoPort = ipNoPort;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public SerializableLocation getSloc() {
		return sloc;
	}
}
