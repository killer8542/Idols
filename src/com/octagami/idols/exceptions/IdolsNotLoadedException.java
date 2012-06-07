package com.octagami.idols.exceptions;

public class IdolsNotLoadedException extends Exception {

	private static final long	serialVersionUID	= 1L;

	public IdolsNotLoadedException() {

		super("Idols plugin isn't loaded!");
	}

}