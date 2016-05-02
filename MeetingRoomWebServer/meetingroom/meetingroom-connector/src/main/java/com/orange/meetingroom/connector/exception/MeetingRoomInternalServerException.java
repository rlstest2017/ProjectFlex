package com.orange.meetingroom.connector.exception;

/**
 * MeetingRoomInternalServerException
 * Exception throws when internal error is produce.
 * @author oab
 */
public class MeetingRoomInternalServerException extends Exception {

	private static final long serialVersionUID = -3297967487080760934L;
	
	public MeetingRoomInternalServerException(String message) {
		super(message);
	}
}
