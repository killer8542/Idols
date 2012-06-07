package com.octagami.idols.executor;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.commands.IdolsCommand;

@SuppressWarnings("unused")
public class MasterCommandExecutor implements CommandExecutor {

	private transient IdolsPlugin	plugin;

	private static final String		commandPath				= "com.octagami.idols.commands.Command";
	private static final String		permissionPrefix		= "idols.";

	public static final String		NOT_IMPLEMENTED_MESSAGE	= "command exists, but has no effect, which is probably a bug";

	public static final Logger		LOGGER					= Logger.getLogger("Idols");

	public MasterCommandExecutor(IdolsPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {

		return onCommandIdols(sender, command, commandLabel, args, IdolsPlugin.class.getClassLoader());
	}

	public boolean onCommandIdols(final CommandSender sender, final Command command, final String commandLabel, final String[] args,
			final ClassLoader classLoader) {

		String commandText = "";
		commandText += sender.getName() + ": /" + commandLabel + " ";
		
		for (int i = 0; i < args.length; i++)
			commandText += args[i] + " ";
		
		plugin.getLogger().info(commandText);

		IdolsCommand cmd;
		try {
			cmd = (IdolsCommand) classLoader.loadClass(commandPath + command.getName()).newInstance();
			cmd.setIdolsPlugin(plugin);

		} catch (Exception ex) {
			sender.sendMessage("The /" + commandLabel + NOT_IMPLEMENTED_MESSAGE);
			LOGGER.log(Level.SEVERE, ("The /" + commandLabel + NOT_IMPLEMENTED_MESSAGE));
			return true;
		}

		try {

			cmd.run(sender, command, commandLabel, args);

			return true;
		} catch (Exception ex) {

			if (!ex.getMessage().isEmpty()) {
				sender.sendMessage(ex.getMessage());
			}
			return true;
		}

	}

}
