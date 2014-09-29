package org.stormdev.gbplugin.plugin.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

public class PlayerServerSender {
	public static void sendToServer(Player player, String server){
		 	ByteArrayOutputStream b = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(b);
	        try {
                out.writeUTF("Connect");
                out.writeUTF(server);
            }
            catch (IOException localIOException1) {
            }
            player.sendPluginMessage(GameBlade.plugin, "BungeeCord", b.toByteArray());
	}
}
