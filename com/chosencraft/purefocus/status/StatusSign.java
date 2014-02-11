package com.chosencraft.purefocus.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

public class StatusSign
{
	
	private static final List<StatusSign> SIGNS = new ArrayList<StatusSign>();
	
	public static void addSign(StatusSign sign)
	{
		SIGNS.add(sign);
		saveSigns();
	}
	
	/**
	 * Saves all the signs
	 */
	public static void saveSigns()
	{
		try
		{
			File file = new File("./plugins/ServerStatus/signs.yml");
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			YamlConfiguration config = new YamlConfiguration();
			
			int i = 0;
			for (StatusSign sign : SIGNS)
			{
				Location loc = sign.getLocation();
				config.set("sign." + i + ".username", sign.getUsername());
				config.set("sign." + i + ".world", loc.getWorld().getName());
				config.set("sign." + i + ".px", loc.getBlockX());
				config.set("sign." + i + ".py", loc.getBlockY());
				config.set("sign." + i + ".pz", loc.getBlockZ());
				
				i++;
			}
			
			config.save(file);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all the signs
	 */
	public static void loadSigns()
	{
		SIGNS.clear();
		File file = new File("./plugins/ServerStatus/signs.yml");
		if (file.exists())
		{
			try
			{
				YamlConfiguration config = new YamlConfiguration();
				config.load(file);
				
				for (int i = 0; true; i++)
				{
					if (!config.isSet("sign." + i))
					{
						break;
					}
					
					StatusSign sign = new StatusSign();
					
					String wrld = config.getString("sign." + i + ".world");
					World world = Bukkit.getWorld(wrld);
					
					int px = config.getInt("sign." + i + ".px");
					int py = config.getInt("sign." + i + ".py");
					int pz = config.getInt("sign." + i + ".pz");
					Location loc = new Location(world, px, py, pz);
					sign.setLocation(loc);
					sign.setUsername(config.getString("sign." + i + ".username"));
					
					SIGNS.add(sign);
					
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static StatusSign getSignAtLocation(Location loc)
	{
		loc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		for (StatusSign sign : SIGNS)
		{
			if (sign.getLocation().equals(loc))
			{
				return sign;
			}
		}
		
		return null;
	}
	
	private String username;
	private Location loc;
	private Sign sign;
	
	/**
	 * Gets the username
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Gets the Location
	 */
	public Location getLocation()
	{
		return loc;
	}
	
	/**
	 * Sets the location
	 */
	public void setLocation(Location loc)
	{
		this.loc = loc;
	}
	
	/**
	 * Sets the username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	/**
	 * Updates the sign
	 */
	public void updateSign()
	{
		// if(sign == null)
		// {
		Block block = loc.getBlock();
		if (block != null)
		{
			BlockState state = block.getState();
			if (state != null && state instanceof Sign)
			{
				sign = (Sign) state;
			}
		}
		// }
		
		if (sign != null)
		{
			
			Status status = Status.getStatus(username);
			for (int i = 0; i < 4; i++)
			{
				String format = Config.SIGN_LAYOUT.get(i);
				if (!format.equals(""))
				{
					sign.setLine(i, format(format, status));
				}
			}
			
			sign.update();
		}
		
	}
	
	/**
	 * Updates all the signs
	 */
	public static void updateAll()
	{
		
		long curTime = System.currentTimeMillis();
		for (Status status : Status.STATUS.values())
		{
			if (curTime - status.timestamp > Config.TIMEOUT)
			{
				status.count = -1;
				status.max = -1;
			}
		}
		for (StatusSign sign : SIGNS)
		{
			sign.updateSign();
		}
	}
	
	/**
	 * Formats the line
	 */
	private String format(String line, Status status)
	{
		return line.replace("{status}", status.max == -1 ? "&coffline" : (status.count + "/" + status.max))
				.replace("{server}", status.name)
				.replace("{motd}", status.motd)
				.replace("{max}", "" + status.max)
				.replace("{count}", "" + status.count)
				.replaceAll("&([0-9a-f|k|l|m|n|o|r])", "\247$1");
	}
	
}
