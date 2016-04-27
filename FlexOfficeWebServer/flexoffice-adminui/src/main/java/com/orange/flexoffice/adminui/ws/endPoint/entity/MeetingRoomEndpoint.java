package com.orange.flexoffice.adminui.ws.endPoint.entity;


import static com.orange.flexoffice.adminui.ws.ParamsConst.MEETINGROOM_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.MEETINGROOMS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.MEETINGROOM_ID_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.flexoffice.adminui.ws.model.MeetingRoom;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomInput;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomOutput;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomSummary;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;

/**
 * Defines all operations available for a resource "meetingroom".
 */
@Path(MEETINGROOMS_PATH)
public interface MeetingRoomEndpoint {

	/**
	 * Gets meetingrooms.
	 * 
	 * @return meeting room summary list.
	 * 
	 * @see MeetingRoom
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<MeetingRoomSummary> getMeetingRooms();
	
	/**
	 * Gets information on a specific meeting room.
	 * 
	 * @param meetingroomId the meeting room ID
	 * 
	 * @return information about a specific meeting room.
	 * 
	 * @see MeetingRoom
	 */
	@GET
	@Path(MEETINGROOM_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MeetingRoom getMeetingRoom(@PathParam(MEETINGROOM_ID_PARAM) String meetingroomId);

	
	
	/**
	 * Add a new meeting room.
	 * 
	 * @param meetingroom the new meeting room
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see MeetingRoom
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MeetingRoomOutput addMeetingRoom(MeetingRoomInput meetingroom);
	
	/**
	 * Modifies a specific meeting room.
	 * To identify a meeting room, a meeting room id is required.
	 * 
	 * @param id a meeting room id. 
	 * @param meetingroom the new <code>meetingroom</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 */
	@PUT
	@Path(MEETINGROOM_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateMeetingRoom(@PathParam(MEETINGROOM_ID_PARAM)String id, MeetingRoomInput meetingroom);

	/**
	 * Deletes a specific meeting room.
	 * To identify a meeting room, a meeting room id is required. 
	 * 
	 * @param id a meeting room id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(MEETINGROOM_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeMeetingRoom(@PathParam(MEETINGROOM_ID_PARAM)String id);

	/**
	 * findByName
	 * @param name
	 * @return
	 * @throws DataNotExistsException
	 */
	// Used for tests
	MeetingRoomDao findByName(String name) throws DataNotExistsException;
	
	/**
	 * Initialise RoomStats tests data in DB
	 * @return true if successfully done
	 */
	// Used for tests
	boolean initRoomStatsTable();
	
}



