package com.orange.flexoffice.business.gatewayapi.exception;

/**
 * Exception throws when trying to save an already existing data.
 * 
 * @author Guillaume Mouricou
 */
public class DataAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public DataAlreadyExistsException(String message) {
		super(message);
	}

}
