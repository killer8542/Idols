package com.octagami.idols.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octagami.idols.IdolsPlugin;


@SuppressWarnings("unused")
public class IdolsCommand {

	private IdolsPlugin		plugin;

	private final String	name;

	// public static final String PLAYER_ONLY_MESSAGE = "this command can only be run by a player";

	protected IdolsCommand(final String name) {
		this.name = name;
	}

	public void setIdolsPlugin(final IdolsPlugin plugin) {
		this.plugin = plugin;
	}

	public String getName() {
		return name;
	}

	public void run(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		runCommand(sender, command, commandLabel, args);
	}

	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		throw new Exception("The Command subclass for this command does not exist");
	}

}
