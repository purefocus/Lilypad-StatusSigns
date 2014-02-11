package com.chosencraft.purefocus.status;

import java.util.Map;
import java.util.WeakHashMap;

import lilypad.client.connect.api.Connect;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerStatus extends JavaPlugin implements Listener
{
	
	private Connect connect;
	
	private TRunnable signupdates, checkin;
	private MessageListener listener;
	
	private static Map<Player, Long> cooldowns = new WeakHashMap<>();
	
	/**
	 * Enables the plugin
	 */
	public void onEnable()
	{
		if (!setupConnect())
		{
			System.out.println("Couldn't get a connection to the cloud!");
		}
		else
		{
			Config.loadConfiguration();
			
			if (Config.IS_RECIEVING)
			{
				StatusSign.loadSigns();
				signupdates = new UpdateTask(this);
				listener = new MessageListener();
				connect.registerEvents(listener);
				
				getCommand("setstatus").setExecutor(new SetSignCommand());
				getServer().getPluginManager().registerEvents(this, this);
			}
			checkin = new CheckinTask(this);
			
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			if (block != null)
			{
				StatusSign sign = StatusSign.getSignAtLocation(block.getLocation());
				if (sign != null)
				{
					Player player = event.getPlayer();
					if (!cooldowns.containsKey(player) || (System.currentTimeMillis() - cooldowns.get(player)) > 1500)
					{
						cooldowns.put(player, System.currentTimeMillis());
						Bukkit.dispatchCommand(event.getPlayer(), "server " + sign.getUsername());
					}
				}
			}
		}
	}
	
	/**
	 * Disable the plugin
	 */
	public void onDisable()
	{
		if (signupdates != null)
		{
			signupdates.stop();
		}
		if (checkin != null)
		{
			checkin.stop();
		}
		if (listener != null)
		{
			connect.unregisterEvents(listener);
		}
	}
	
	/**
	 * Gets the Connect Service
	 */
	public boolean setupConnect()
	{
		RegisteredServiceProvider<Connect> rsp = Bukkit.getServicesManager().getRegistration(Connect.class);
		return (connect = rsp.getProvider()) != null;
	}
	
	/**
	 * Returns the Connect Service
	 */
	public Connect getConnect()
	{
		return connect;
	}
	
}
