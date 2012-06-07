package com.octagami.idols.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlugin;

public class Commandidols extends IdolsCommand {

	public Commandidols() {
		super("idols");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
		
		if (args.length > 0) {

			if (args[0].equals("help")) {
				
				sender.sendMessage("Idols: v" + IdolsPlugin.VERSION);
			}
			else if (args[0].equals("reload")) {
				
				if ( sender.hasPermission("idols.reload") ) {
					
					Idols.getPlugin().reload();
					sender.sendMessage(IdolsPlugin.NAME + " has been reloaded");
					
				} else {
					
					sender.sendMessage("You don't have permission to do that.");
				}

			}
					

		} else {

			sender.sendMessage("Idols: v" + IdolsPlugin.VERSION);
		}

		
	}

}
