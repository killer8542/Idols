package com.octagami.idols.commands;

import java.util.Date;
import java.util.concurrent.TimeUnit;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octagami.idols.util.Util;

public class Commandhistory extends IdolsCommand {

	public Commandhistory() {
		super("history");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		if (sender.hasPermission("idols.history")) {
			
			OfflinePlayer targetOfflinePlayer;
			
			if (args.length < 1) {

				targetOfflinePlayer = Bukkit.getServer().getOfflinePlayer(sender.getName());
			
			} else {

				if (!sender.hasPermission("idols.history.others")) {
					
					sender.sendMessage(ChatColor.RED + " You don't have permission to check the history of other players");
					return;
				}
				
				targetOfflinePlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
				
			}
			
			if (targetOfflinePlayer.hasPlayedBefore()) {
				
				Date firstPlayedDate = new Date(targetOfflinePlayer.getFirstPlayed());
				
				long totalDays = Util.getTimeRemaining(firstPlayedDate, new Date(), TimeUnit.DAYS);
				
				sender.sendMessage(targetOfflinePlayer.getName() + " has been on the server for " + Long.toString(totalDays) + " days, starting on " + firstPlayedDate.toString());
				
			} else {

				sender.sendMessage(ChatColor.RED + args[0] + " has never played on this server before.");
			}
			
		}
		
	}

}
