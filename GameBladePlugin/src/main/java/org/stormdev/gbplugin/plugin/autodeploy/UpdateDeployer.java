package org.stormdev.gbplugin.plugin.autodeploy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.stormdev.servermanager.api.APIProvider;
import org.stormdev.servermanager.api.events.MessageReceiveEvent;
import org.stormdev.servermanager.api.events.ReceivedMessage;
import org.stormdev.servermanager.api.listeners.SMEventHandler;
import org.stormdev.servermanager.api.listeners.SMListener;
import org.stormdev.servermanager.api.messaging.MessageRecipient;

public class UpdateDeployer implements SMListener {
	private static final String TITLE = "deployUpdate";
	private static final String SEPARATOR = "|";
	
	public UpdateDeployer(){
		APIProvider.getAPI().getEventManager().registerListener(this);
	}
	
	public static void update(String plugName, String URL){
		try {
			APIProvider.getAPI().getMessenger().sendMessage(MessageRecipient.ALL, TITLE, plugName+SEPARATOR+URL);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		handleUpdate(plugName, URL);
	}
	
	private static void handleUpdate(String name, String URL){
		boolean doUpdate = false;
		for(Plugin p:Bukkit.getPluginManager().getPlugins()){
			if(p.getName().equalsIgnoreCase(name)){
				doUpdate = true;
			}
		}
		if(!doUpdate){
			return;
		}
		
		URL url;
		try {
			url = new URL(URL);
		} catch (MalformedURLException e) {
			System.out.println("Error in handling auto update deployment: Malformed URL");
			return;
		}
		
		String fileName = url.getFile();
		if(fileName == null || fileName.length() < 1){
			System.out.println("Error in handling auto update deployment: Unable to determine FileName to download");
			return;
		}
		
		File dest = new File(Bukkit.getUpdateFolderFile() + File.separator + fileName);
		dest.getParentFile().mkdirs();
		if(!dest.exists()){
			try {
				dest.createNewFile();
			} catch (IOException e) {
				System.out.println("Error in handling auto update deployment: Could not create file to download to");
				return;
			}
		}
		System.out.println("Downloading update for plugin: "+name);
		
		try {
			IOUtils.copy(url.openStream(), new BufferedWriter(new FileWriter(dest)));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Update failed...");
			return;
		}
		
		Bukkit.getServer().shutdown();
		System.exit(0);
		return;
	}
	
	@SMEventHandler
	void incomingMessage(MessageReceiveEvent event){
		ReceivedMessage rm = event.getMessage();
		if(!rm.getTitle().equals(TITLE)){
			return;
		}
		
		String body = rm.getMessage();
		String[] parts = body.split(Pattern.quote(SEPARATOR));
		if(parts.length != 2){
			System.out.println("Error in update deployment! Invalid message!");
			return;
		}
		
		handleUpdate(parts[0], parts[1]);
	}
}
