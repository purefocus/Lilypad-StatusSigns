package com.chosencraft.purefocus.status;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

/**
 * 
 * the task to send the message to the hub with server information
 * 
 * @author Brandon
 * 
 */

public class CheckinTask implements TRunnable
{
	
	private ServerStatus status;
	private BukkitTask task;
	
	public CheckinTask(ServerStatus status)
	{
		this.status = status;
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(status, this, 1, Config.REFRESH_RATE);
	}
	
	/**
	 * Stops the task
	 */
	
	public void stop()
	{
		task.cancel();
	}
	
	/**
	 * Converts player count and max players into a byte array to send as a
	 * message
	 */
	
	@Override
	public void run()
	{
		
		Connect connect = status.getConnect();
		if (connect.isConnected())
		{
			byte[] motd_bytes = Bukkit.getMotd().getBytes();
			
			byte[] data = new byte[motd_bytes.length + 4];
			
			int count = Bukkit.getOnlinePlayers().length;
			data[0] = (byte) (count & 0x7f);
			data[1] = (byte) ((count >> 7) & 0x7f);
			
			int maxCount = Bukkit.getMaxPlayers();
			data[2] = (byte) (maxCount & 0x7f);
			data[3] = (byte) ((maxCount >> 7) & 0x7f);
			try
			{
				for (int i = 4; i < data.length; i++)
				{
					data[i] = motd_bytes[i - 4];
				}
			}
			catch (Exception e)
			{
				System.out.printf("%d, %d", motd_bytes.length, data.length);
			}
			
			try
			{
				connect.request(new MessageRequest(Config.SERVERS, "pfs", data));
			}
			catch (RequestException e)
			{
			}
		}
		
	}
}
