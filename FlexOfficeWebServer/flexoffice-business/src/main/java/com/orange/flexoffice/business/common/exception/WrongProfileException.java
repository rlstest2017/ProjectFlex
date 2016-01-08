package com.orange.flexoffice.business.common.exception;

/**
 * Exception throws when profile is not exist.
 * 
 * @author oab
 */
public class WrongProfileException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public WrongProfileException(String message) {
		super(message);
	}

}
