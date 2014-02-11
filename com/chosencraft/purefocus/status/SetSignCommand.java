package com.chosencraft.purefocus.status;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSignCommand implements CommandExecutor
{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		
		if (sender instanceof Player && ((Player) sender).isOp())
		{
			
			Player player = (Player) sender;
			
			if (args.length == 1)
			{
				String server = args[0];
				
				@SuppressWarnings("deprecation")
				Block b = player.getTargetBlock(null, 10);
				BlockState state = b.getState();
				if (state instanceof Sign)
				{
					
					StatusSign sign = new StatusSign();
					sign.setUsername(server);
					sign.setLocation(b.getLocation());
					
					StatusSign.addSign(sign);
					
					player.sendMessage("Defined sign " + sign.getUsername());
					
				}
				else
				{
					player.sendMessage("That is not a sign!");
				}
			}
			else
			{
				player.sendMessage("Please specify a server! /setstatus <server>");
			}
		}
		return false;
	}
	
}
