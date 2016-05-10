package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import com.orange.meetingroom.gui.ws.endPoint.entity.MeetingRoomEndpoint;
import com.orange.meetingroom.gui.ws.model.BookingSetInput;
import com.orange.meetingroom.gui.ws.model.BookingSetOutput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateInput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateOutput;
import com.orange.meetingroom.gui.ws.model.MeetingRoom;
import com.orange.meetingroom.gui.ws.model.MeetingRooms;

/**
 * MeetingRoomEndpointImpl
 * @author oab
 *
 */
public class MeetingRoomEndpointImpl implements MeetingRoomEndpoint {

	@Override
	public MeetingRoom getMeetingRoomBookings(String meetingRoomExternalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeetingRooms getBookings(String dashboardMacAddress, Integer maxBookings, Integer startDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingSetOutput setBooking(String meetingRoomExternalId, BookingSetInput bookingSetInput) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingUpdateOutput cancelBooking(String meetingRoomExternalId, BookingUpdateInput bookingUpdateInput) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingUpdateOutput confirmBooking(String meetingRoomExternalId, BookingUpdateInput bookingUpdateInput) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
