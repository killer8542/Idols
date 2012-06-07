package com.octagami.idols.exceptions;

import org.bukkit.command.Command;

public class NotEnoughArgumentsException extends Exception {

	private static final long	serialVersionUID	= 2L;

	public NotEnoughArgumentsException(Command command, String commandLabel) {

		super(command.getUsage().replaceAll("<command>", commandLabel));
	}

}
