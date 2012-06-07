package com.octagami.idols.exceptions;

public class BadArgumentsException extends Exception {

	private static final long	serialVersionUID	= 2L;

	public BadArgumentsException(String badArg) {

		super("The command argument \"" + badArg + "\" does not exist");
	}

}
