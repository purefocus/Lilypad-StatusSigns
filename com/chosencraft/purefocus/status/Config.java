package com.chosencraft.purefocus.status;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
	/**
	 * The servers to send the updates to
	 */
	public static List<String> SERVERS;
	
	/**
	 * rate to send the information
	 */
	public static long REFRESH_RATE;
	
	/**
	 * Whether or not the server is recieving updates
	 */
	public static boolean IS_RECIEVING;
	
	/**
	 * Layout of the signs
	 */
	public static List<String> SIGN_LAYOUT;
	
	/**
	 * How long to wait for a message before marking offline
	 */
	public static long TIMEOUT;
	
	/**
	 * load the configuration
	 */
	public static void loadConfiguration()
	{
		try
		{
			File file = new File("./plugins/ServerStatus/config.yml");
			if (!file.exists())
			{
				new File("./plugins/ServerStatus").mkdirs();
				file.createNewFile();
			}
			
			YamlConfiguration config = new YamlConfiguration();
			config.load(file);
			
			SERVERS = config.getStringList("servers");
			IS_RECIEVING = config.getBoolean("recieving");
			TIMEOUT = config.getLong("timeout");
			REFRESH_RATE = config.getLong("refresh_rate");
			
			SIGN_LAYOUT = config.getStringList("signLayout");
			
			TIMEOUT = ((REFRESH_RATE + TIMEOUT) / 20) * 1000;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
