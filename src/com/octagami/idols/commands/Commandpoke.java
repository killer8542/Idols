package com.octagami.idols.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octagami.idols.exceptions.NotEnoughArgumentsException;

public class Commandpoke extends PlayerCommand {

	public Commandpoke() {
		super("poke");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		Player player = (Player) sender;

		Player other = null;

		if (args.length > 0) {

			other = Bukkit.getServer().getPlayer(args[0]);

			if (other == null) {

				sender.sendMessage(ChatColor.RED + args[0] + " is not online!");

			} else if (player.equals(other)) {

				sender.sendMessage("You poke yourself. Ouch!");

			} else {

				sender.sendMessage("You poke " + other.getName());
				other.sendMessage("You have been poked by " + player.getName() + "!");
			}

		} else {

			throw new NotEnoughArgumentsException(command, commandLabel);
		}

	}

}
