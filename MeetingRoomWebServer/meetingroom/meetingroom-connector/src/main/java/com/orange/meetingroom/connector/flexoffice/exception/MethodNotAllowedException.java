package com.orange.meetingroom.connector.flexoffice.exception;

/**
 * Exception throws when trying retrieve not existing data.
 * 
 * @author oab
 */
public class MethodNotAllowedException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public MethodNotAllowedException(String message) {
		super(message);
	}

}
