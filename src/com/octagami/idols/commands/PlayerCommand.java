package com.octagami.idols.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octagami.idols.exceptions.PlayerOnlyCommandException;

public class PlayerCommand extends IdolsCommand {

	protected PlayerCommand(final String name) {
		super(name);
	}

	@Override
	public void run(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		if (sender instanceof Player) {

			runCommand(sender, command, commandLabel, args);

		} else {

			throw new PlayerOnlyCommandException();
		}

	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		throw new Exception("The PlayerCommand subclass for this command does not exist");
	}

}
