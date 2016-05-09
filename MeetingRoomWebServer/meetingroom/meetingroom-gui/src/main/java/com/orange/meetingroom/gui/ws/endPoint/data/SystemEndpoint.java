package com.orange.meetingroom.gui.ws.endPoint.data;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.orange.meetingroom.gui.ws.PathConst.SYSTEM_PATH;

/**
 * Defines all operations available for a resource "system".
 */
@Path(SYSTEM_PATH)
public interface SystemEndpoint {
	
	/**
	 * Gets system.
	 * 
	 * @return System.
	 * 
	 * @see System
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	com.orange.meetingroom.gui.ws.model.SystemReturn getSystem();
	
	@OPTIONS
	@Path("{path : .*}")
	Response options();
	
		
}



