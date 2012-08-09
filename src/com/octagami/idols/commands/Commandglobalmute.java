package com.octagami.idols.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octagami.idols.exceptions.BadArgumentsException;

public class Commandglobalmute extends IdolsCommand {

	public Commandglobalmute() {
		super("globalmute");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command,
			final String commandLabel, final String[] args) throws Exception {

		if (sender.hasPermission("idols.globalmute")) {
			
			if (args.length == 0) {

				if (!plugin.getIdolsConfig().isGlobalMuteOn()) {
					sender.sendMessage("Global mute is now enabled");
					enableGlobalMute();
				} else {
					sender.sendMessage("Global mute is now disabled");
					disableGlobalMute();
				}
				
			} else {

				if (args[0].equals("off") || args[0].equals("disable")) {

					if (plugin.getIdolsConfig().isGlobalMuteOn()) {
						disableGlobalMute();
						sender.sendMessage("Global mute is now disabled");
					} else {
						sender.sendMessage("Global mute is already disabled");
					}
					
				} else if (args[0].equals("on") || args[0].equals("enable")) {

					if (!plugin.getIdolsConfig().isGlobalMuteOn()) {
						enableGlobalMute();
						sender.sendMessage("Global mute is now enabled");
					} else {
						sender.sendMessage("Global mute is already enabled");
					}
					
				} else {
					throw new BadArgumentsException(args[0]);
				}

			}

		}
		
		
		

	}

	private void enableGlobalMute() {
		
		plugin.getIdolsConfig().setGlobalMute(true);
		
		plugin.getServer().broadcastMessage(ChatColor.RED + "* * * A global chat mute is now in effect * * *");
		
	}
	
	
	private void disableGlobalMute() {
		
		plugin.getIdolsConfig().setGlobalMute(false);
		
		plugin.getServer().broadcastMessage(ChatColor.RED + "* * * The global chat mute has been lifted * * *");
	}
}
