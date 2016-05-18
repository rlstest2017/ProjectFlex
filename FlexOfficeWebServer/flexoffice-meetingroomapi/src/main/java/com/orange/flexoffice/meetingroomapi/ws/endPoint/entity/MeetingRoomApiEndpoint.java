package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity;

import static com.orange.flexoffice.meetingroomapi.ws.ParamsConst.MEETINGROOM_EXTERNAL_ID_PARAM;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.MEETINGROOMS_PATH;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.MEETINGROOM_EXTERNAL_ID_PATH;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.TIMEOUT_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.flexoffice.meetingroomapi.ws.model.MeetingRoomInput;

/**
 * Defines all operations available for a resource "meetingroom".
 */
@Path(MEETINGROOMS_PATH)
public interface MeetingRoomApiEndpoint {
	
	/**
	 * List meetingrooms witch status is in timeout (last_measure_date)
	 * 
	 * @return String
	 * 
	 */
	@GET
	@Path(TIMEOUT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	List<String> getTimeout();

	
	/**
	 * update meetingroom data
	 * 
	 * @param id a meetingroom external id. 
	 * @param the updated meetingroom <code>meetingroom</code>
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@PUT
	@Path(MEETINGROOM_EXTERNAL_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateData(@PathParam(MEETINGROOM_EXTERNAL_ID_PARAM)String externalId, MeetingRoomInput meetingRoom);

	
	// used for tests
	boolean executeInitTestFile();

	// used for tests
	/*GatewayDto findByMacAddress(String macAddress)  throws DataNotExistsException;

	// used for tests
	boolean initTeachinSensorsTable();*/

}



