package com.orange.flexoffice.userui.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.orange.flexoffice.userui.ws.ParamsConst.ROOM_ID_PARAM;
import static com.orange.flexoffice.userui.ws.PathConst.ROOMS_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.ROOM_ID_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.ROOM_ID_RESERVE_PATH;

import java.util.List;

import static com.orange.flexoffice.userui.ws.PathConst.ROOM_ID_CANCEL_PATH;

import com.orange.flexoffice.userui.ws.model.Room;
import com.orange.flexoffice.userui.ws.model.RoomSummary;

/**
 * Defines all operations available for a resource "user".
 */
@Path(ROOMS_PATH)
public interface RoomEndpoint {

	/**
	 * Gets rooms.
	 * 
	 * @return room summary list.
	 * 
	 * @see RoomSummary
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<RoomSummary> getRooms();

	/**
	 * Gets information on a specific room.
	 * 
	 * @param roomId
	 *            the room ID
	 * 
	 * @return information about a specific room.
	 * 
	 * @see Room
	 */
	@GET
	@Path(ROOM_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Room getRoom(@PathParam(ROOM_ID_PARAM) String roomId);

	/**
	 * Reserve a room.
	 * 
	 * @param roomId
	 *            the room ID
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@POST
	@Path(ROOM_ID_RESERVE_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	Response reserveRoom(@PathParam(ROOM_ID_RESERVE_PATH) String roomId);

	/**
	 * Cancel reservation of a room.
	 * 
	 * @param roomId
	 *            the room ID
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@POST
	@Path(ROOM_ID_CANCEL_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	Response cancelRoom(@PathParam(ROOM_ID_CANCEL_PATH) String roomId);


	
	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	boolean executeInitTestFile();
}



