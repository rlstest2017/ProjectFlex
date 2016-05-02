package com.orange.meetingroom.connector.exception;

/**
 * Exception throws when erroFlag = true from PHP response.
 * 
 * @author oab
 */
public class PhpInternalServerException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public PhpInternalServerException(String message) {
		super(message);
	}

}
