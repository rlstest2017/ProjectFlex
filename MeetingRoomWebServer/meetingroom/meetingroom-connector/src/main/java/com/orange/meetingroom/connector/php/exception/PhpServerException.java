package com.orange.meetingroom.connector.php.exception;

/**
 * Exception throws when erroFlag = true from PHP response.
 * 
 * @author oab
 */
public class PhpServerException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public PhpServerException(String message) {
		super(message);
	}

}
