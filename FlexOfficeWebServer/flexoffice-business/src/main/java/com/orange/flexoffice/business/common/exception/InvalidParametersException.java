package com.orange.flexoffice.business.common.exception;

/**
 * Exception throws when parameters are not valid (null or wrong).
 * 
 * @author oab
 */
public class InvalidParametersException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public InvalidParametersException(String message) {
		super(message);
	}

}
