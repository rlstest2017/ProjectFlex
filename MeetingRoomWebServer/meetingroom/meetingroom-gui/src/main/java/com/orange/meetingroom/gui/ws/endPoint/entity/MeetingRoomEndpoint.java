package com.orange.meetingroom.gui.ws.endPoint.entity;

import static com.orange.meetingroom.gui.ws.ParamsConst.MEETINGROOM_EXTERNAL_ID_PARAM;
import static com.orange.meetingroom.gui.ws.ParamsConst.FORCE_UPDATE_CACHE;
import static com.orange.meetingroom.gui.ws.ParamsConst.DASHBOARD_MAC_ADDRESS_PARAM;
import static com.orange.meetingroom.gui.ws.PathConst.MEETINGROOMS_PATH;
import static com.orange.meetingroom.gui.ws.PathConst.BOOKINGS_PATH;
import static com.orange.meetingroom.gui.ws.PathConst.RESERVE_PATH;
import static com.orange.meetingroom.gui.ws.PathConst.CANCEL_PATH;
import static com.orange.meetingroom.gui.ws.PathConst.CONFIRM_PATH;
import static com.orange.meetingroom.gui.ws.PathConst.MEETINGROOMS_EXTERNAL_ID_PATH;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.meetingroom.gui.ws.model.BookingSetInput;
import com.orange.meetingroom.gui.ws.model.BookingSetOutput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateInput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateOutput;
import com.orange.meetingroom.gui.ws.model.MeetingRoom;
import com.orange.meetingroom.gui.ws.model.MeetingRooms;

/**
 * Defines all operations available for a resource "meetingroom".
 */
@Path(MEETINGROOMS_PATH)
public interface MeetingRoomEndpoint {

	/**
	 * Get information on a specific meetingroom.
	 * 
	 * @param meetingRoomExternalId
	 *            the meetingRoom external ID
	 * 
	 * @return information about a specific meetingroom.
	 * 
	 * @see MeetingRoom
	 */
	@GET
	@Path(MEETINGROOMS_EXTERNAL_ID_PATH + BOOKINGS_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MeetingRoom getMeetingRoomBookings(@PathParam(MEETINGROOM_EXTERNAL_ID_PARAM) String meetingRoomExternalId, @QueryParam(FORCE_UPDATE_CACHE) Boolean forceUpdateCache);

	/**
	 * Get information on meetingrooms for dashboard.
	 * 
	 * 
	 * @return information about meetingrooms for dashboard.
	 * 
	 * @see MeetingRooms
	 */
	@GET
	@Path(BOOKINGS_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MeetingRooms getBookings(@QueryParam(DASHBOARD_MAC_ADDRESS_PARAM) String dashboardMacAddress);
	
	/**
	 * Set a specific booking.
	 * 
	 * @param meetingRoomExternalId
	 * 			  a meetingRoom external id. 
	 * @param bookingSetInput
	 *     
	 * @return If ok, a <code>BookingSetOutput</code> with a status code 201 Create & Location.
	 * 
	 * @see BookingSetOutput
	 */
	@POST
	@Path(MEETINGROOMS_EXTERNAL_ID_PATH + BOOKINGS_PATH + RESERVE_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BookingSetOutput setBooking(@PathParam(MEETINGROOM_EXTERNAL_ID_PARAM)String meetingRoomExternalId, BookingSetInput bookingSetInput);

	/**
	 * Cancel a specific booking.
	 * 
	 * @param meetingRoomExternalId
	 * 			  a meetingRoom external id. 
	 * @param BookingUpdateInput
	 *     
	 * @return If ok, a <code>BookingUpdateOutput</code> with a status code 201 Create & Location.
	 * 
	 * @see BookingUpdateOutput
	 */
	@POST
	@Path(MEETINGROOMS_EXTERNAL_ID_PATH + BOOKINGS_PATH + CANCEL_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BookingUpdateOutput cancelBooking(@PathParam(MEETINGROOM_EXTERNAL_ID_PARAM)String meetingRoomExternalId, BookingUpdateInput bookingUpdateInput);
	
	/**
	 * Confirm a specific booking.
	 * 
	 * @param meetingRoomExternalId
	 * 			  a meetingRoom external id. 
	 * @param BookingUpdateInput
	 *     
	 * @return If ok, a <code>BookingUpdateOutput</code> with a status code 201 Create & Location.
	 * 
	 * @see BookingUpdateOutput
	 */
	@POST
	@Path(MEETINGROOMS_EXTERNAL_ID_PATH + BOOKINGS_PATH + CONFIRM_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BookingUpdateOutput confirmBooking(@PathParam(MEETINGROOM_EXTERNAL_ID_PARAM)String meetingRoomExternalId, BookingUpdateInput bookingUpdateInput);

	@OPTIONS
	@Path("{path : .*}")
	Response options();
	
	// for test
	public boolean checkMeetingRoomsStatusTimeOutTestMethod();

}



