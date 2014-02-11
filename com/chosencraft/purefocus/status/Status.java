package com.chosencraft.purefocus.status;

import java.util.HashMap;
import java.util.Map;

public class Status
{
	public static final Map<String, Status> STATUS = new HashMap<String, Status>();

	/**
	 * Gets the status of a server or creates a new one if one doesn't exist
	 */
	public static Status getStatus(String name)
	{
		Status status = STATUS.get(name);
		if (status == null)
		{
			status = new Status();
			status.name = name;
			status.timestamp = System.currentTimeMillis();

			STATUS.put(name, status);
		}
		return status;
	}

	public String name;
	public int count = -1;
	public int max = -1;
	public long timestamp;
	public String motd = "";

	/**
	 * converts the class into a string, used for debugging
	 */
	public String toString()
	{
		return String.format("name=%s, count=%d, max=%d", name, count, max);
	}
}
