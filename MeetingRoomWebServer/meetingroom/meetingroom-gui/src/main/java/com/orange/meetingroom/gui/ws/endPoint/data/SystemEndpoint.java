package com.orange.meetingroom.gui.ws.endPoint.data;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.meetingroom.gui.ws.model.SystemCurrentDate;
import com.orange.meetingroom.gui.ws.model.SystemRemoteMacAddress;
import com.orange.meetingroom.gui.ws.model.SystemReturn;

import static com.orange.meetingroom.gui.ws.PathConst.CURRENT_DATE;
import static com.orange.meetingroom.gui.ws.PathConst.MAC_ADDRESS;
import static com.orange.meetingroom.gui.ws.PathConst.SYSTEM_PATH;

/**
 * Defines all operations available for a resource "system".
 */
@Path(SYSTEM_PATH)
public interface SystemEndpoint {
	
	static final String ORIGIN = "Origin";
	
	/**
	 * Gets system.
	 * 
	 * @return System.
	 * 
	 * @see System
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	SystemReturn getSystem();
	
	/**
	 * Gets system currentDate.
	 * 
	 * @return SystemCurrentDate .
	 * 
	 * @see SystemCurrentDate
	 */
	@GET
	@Path(CURRENT_DATE)
	@Produces(MediaType.APPLICATION_JSON)
	SystemCurrentDate getSystemCurrentDate();
	
	/**
	 * Gets system macAddress.
	 * 
	 * @return SystemRemoteMacAddress.
	 * 
	 * @see SystemRemoteMacAddress
	 */
	@GET
	@Path(MAC_ADDRESS)
	@Produces(MediaType.APPLICATION_JSON)
	SystemRemoteMacAddress getSystemRemoteMacAddress(@Context HttpServletRequest req);
	
	
	@OPTIONS
	@Path("{path : .*}")
	Response options();
		
}



