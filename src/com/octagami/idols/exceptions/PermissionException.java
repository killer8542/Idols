package com.octagami.idols.exceptions;

public class PermissionException extends Exception {

	private static final long	serialVersionUID	= 3L;

	public PermissionException(String permission) {

		super("You don't have the " + permission + " permission");
	}

}
