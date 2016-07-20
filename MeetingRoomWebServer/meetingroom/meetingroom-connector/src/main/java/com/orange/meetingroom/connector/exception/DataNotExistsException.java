package com.orange.meetingroom.connector.exception;

/**
 * Exception throws when trying retrieve not existing data.
 * 
 * @author oab
 */
public class DataNotExistsException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public DataNotExistsException(String message) {
		super(message);
	}

}
