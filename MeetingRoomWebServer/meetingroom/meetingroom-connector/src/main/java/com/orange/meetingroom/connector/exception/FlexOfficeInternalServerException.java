package com.orange.meetingroom.connector.exception;

/**
 * FlexOfficeInternalServerException
 * Exception throws when internal error in flexoffice server is produce.
 * @author oab
 */
public class FlexOfficeInternalServerException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public FlexOfficeInternalServerException(String message) {
		super(message);
	}
}
