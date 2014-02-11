package com.chosencraft.purefocus.status;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * 
 * the task to update the status signs
 * 
 * @author Brandon
 * 
 */

public class UpdateTask implements TRunnable
{
	
	private BukkitTask   task;
	
	public UpdateTask(ServerStatus status) {
		task = Bukkit.getScheduler().runTaskTimer(status, this, Config.REFRESH_RATE, Config.REFRESH_RATE);
	}
	
	/**
	 * Stops the task
	 */
	
	public void stop()
	{
		task.cancel();
	}
	
	/**
	 * Updates all the signs
	 */
	
	@Override
	public void run()
	{
		StatusSign.updateAll();
		
	}
}
