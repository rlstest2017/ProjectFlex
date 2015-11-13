package com.orange.flexoffice.adminui.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.orange.flexoffice.adminui.ws.ParamsConst.ROOM_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.ROOMS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.ROOM_ID_PATH;

import java.util.List;

import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.model.User;

/**
 * Defines all operations available for a resource "room".
 */
@Path(ROOMS_PATH)
public interface RoomEndpoint {
	
	/**
	 * Gets users.
	 * 
	 * @return user.
	 * 
	 * @see User
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
	
	
}



