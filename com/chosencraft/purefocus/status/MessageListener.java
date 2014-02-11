package com.chosencraft.purefocus.status;

import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.event.EventListener;

public class MessageListener
{
	
	/**
	 * Used for testing message compressing
	 */
	public static void main(String[] args)
	{
		byte[] data = new byte[4];
		
		int count1 = 5000;
		int count2 = 153;
		
		data[0] = (byte) (count1 & 0x7f);
		data[1] = (byte) ((count1 >> 7) & 0x7f);
		
		int c1 = data[0] | (data[1] << 7);
		
		data[2] = (byte) (count2 & 0x7f);
		data[3] = (byte) ((count2 >> 7) & 0x7f);
		
		int c2 = data[2] | (data[3] << 7);
		
		System.out.printf("%d/%d", c1, c2);
		
	}
	
	/**
	 * Listens for incoming messages
	 */
	@EventListener
	public void onMessage(MessageEvent event)
	{
		if (event.getChannel().equals("pfs"))
		{
			String server = event.getSender();
			
			byte[] data = event.getMessage();
			
			int count = data[0] | (data[1] << 7);
			int max = data[2] | (data[3] << 7);
			
			Status status = Status.getStatus(server);
			status.max = max;
			status.count = count;
			status.timestamp = System.currentTimeMillis();
			
			if (data.length > 4)
			{
				byte[] motd_bytes = new byte[data.length - 4];
				for (int i = 4; i < data.length; i++)
				{
					motd_bytes[i - 4] = data[i];
				}
				
				status.motd = new String(motd_bytes);
			}
			else
			{
				status.motd = "";
			}
			
		}
	}
}
