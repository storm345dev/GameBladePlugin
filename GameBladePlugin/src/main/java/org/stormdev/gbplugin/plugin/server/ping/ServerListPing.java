package org.stormdev.gbplugin.plugin.server.ping;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author zh32
 */
public class ServerListPing {
	
	/*
	 * mk1:
	 * localhost:4011
	 * 
	 * mk2:
	 * localhost:4012
	 * 
	 * mk3:
	 * localhost:4013
	 * 
	 * mk4:
	 * localhost:4014
	 */
    
    private static int timeout = 5000;
    private static Gson gson = new GsonBuilder().create();
    
    public static StatusResponse fetchData(InetSocketAddress host) throws IOException {
        Socket socket = null;
        OutputStream oStr = null;
        InputStream inputStream = null;
        StatusResponse response = null;
        
        try {
            socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(host, timeout);

            oStr = socket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(oStr);
            
            inputStream = socket.getInputStream();
            DataInputStream dIn = new DataInputStream(inputStream);
            
            sendPacket(dataOut, prepareHandshake(host));
            sendPacket(dataOut, preparePing());
            
            response = receiveResponse(dIn);
            
            dIn.close();
            dataOut.close();
            
        } catch (Exception ex) {
            //if (plugin.getData().isDebugmode()) Bukkit.getLogger().log(Level.SEVERE, "[TeleportSigns] Can't check server " + host.toString(), ex);
        } finally {
            if (oStr != null) {
                oStr.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        return response;
    }
    
    private static StatusResponse receiveResponse(DataInputStream dIn) throws IOException { 
//    	System.out.println("Recieving response...");
        int size = readVarInt(dIn);
        int packetId = readVarInt(dIn);
        
        if (packetId != 0x00) {
            throw new IOException("Invalid packetId");
        }
        
        int stringLength = readVarInt(dIn);
        
        if (stringLength < 1) {
            throw new IOException("Invalid string length.");
        }
        
//        System.out.println("Apparently all valid");
        
        byte[] responseData = new byte[stringLength];     
        dIn.readFully(responseData);    
        String jsonString = new String(responseData, Charset.forName("utf-8")); 
        
//        System.out.println(jsonString);
        
        StatusResponse response = gson.fromJson(jsonString, StatusResponse.class);
        
//        System.out.println("Parsed: "+response != null);
        return response;
    }
    
    private static void sendPacket(DataOutputStream out, byte[] data) throws IOException {
        writeVarInt(out, data.length);
        out.write(data);
    }
    
    private static byte[] preparePing() throws IOException {
        return new byte[] {0x00};
    }
    
    private static byte[] prepareHandshake(InetSocketAddress host) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(bOut);
        bOut.write(0x00); //packet id
        writeVarInt(handshake, 4); //protocol version
        writeString(handshake, host.getHostString());
        handshake.writeShort(host.getPort());
        writeVarInt(handshake, 1); //target state 1       
        return bOut.toByteArray();
    }
    
    public static void writeString(DataOutputStream out, String string) throws IOException {
        writeVarInt(out, string.length());
        out.write(string.getBytes(Charset.forName("utf-8")));
    }
    
    public static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }
 
    public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
              out.write(paramInt);
              return;
            }

            out.write(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }  
    
    /**
    *
    * @author zh32 <zh32 at zh32.de>
    */
   public static class StatusResponse {
       private String description;
       private Players players;
       private Version version;
       private String favicon;
       private int time;

	public class Players {
           private int max;
           private int online;
           private List<Player> sample;
           
		public int getMax() {
			return max;
		}
		public void setMax(int max) {
			this.max = max;
		}
		public int getOnline() {
			return online;
		}
		public void setOnline(int online) {
			this.online = online;
		}
		public List<Player> getSample() {
			return sample;
		}
		public void setSample(List<Player> sample) {
			this.sample = sample;
		}     
       }
       
       public class Player {
           private String name;
           private String id;
           
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
       }
       
       public class Version {
           private String name;
           private String protocol;
           
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
       }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Players getPlayers() {
		return players;
	}

	public void setPlayers(Players players) {
		this.players = players;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
   }
}
