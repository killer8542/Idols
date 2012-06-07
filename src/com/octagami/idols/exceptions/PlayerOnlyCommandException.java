package com.octagami.idols.exceptions;

public class PlayerOnlyCommandException extends Exception {

	private static final long	serialVersionUID	= 3L;

	public PlayerOnlyCommandException() {

		super("This command can only be run by a player");
	}

}
