package com.octagami.idols.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octagami.idols.Idols;
import com.octagami.idols.exceptions.BadArgumentsException;

public class Commandspawners extends IdolsCommand {

	public Commandspawners() {
		super("spawners");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command,
			final String commandLabel, final String[] args) throws Exception {

		if (sender.hasPermission("idols.spawners")) {
			
			if (args.length == 0) {

				if (Idols.getPlugin().getIdolsConfig().areSpawnersDisabled()) {
					sender.sendMessage("Mob spawners are: Disabled");
				} else {
					sender.sendMessage("Mob spawners are: Enabled");
				}
			} else {

				if (args[0].equals("off") || args[0].equals("disable")) {

					if (!Idols.getPlugin().getIdolsConfig().areSpawnersDisabled()) {
						Idols.getPlugin().getIdolsConfig().setDisableSpawners(true);
						sender.sendMessage("Mob spawners are now disabled");
					} else {
						sender.sendMessage("Mob spawners are already disabled");
					}
				} else if (args[0].equals("on") || args[0].equals("enable")) {

					if (Idols.getPlugin().getIdolsConfig().areSpawnersDisabled()) {
						Idols.getPlugin().getIdolsConfig().setDisableSpawners(false);
						sender.sendMessage("Mob spawners are now enabled");
					} else {
						sender.sendMessage("Mob spawners are already enabled");
					}
				} else {
					throw new BadArgumentsException(args[0]);
				}

			}

		}

	}

}
