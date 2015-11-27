package com.orange.flexoffice.business.common.exception;

/**
 * Exception throws when trying to save an already existing data.
 * 
 * @author oab
 */
public class IntegrityViolationException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public IntegrityViolationException(String message) {
		super(message);
	}

}
