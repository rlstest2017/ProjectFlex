package com.orange.meetingroom.business.service.exception;

/**
 * Exception throws when trying retrieve not existing data.
 * 
 * @author oab
 */
public class DateNotInSlotTimeException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public DateNotInSlotTimeException(String message) {
		super(message);
	}

}
