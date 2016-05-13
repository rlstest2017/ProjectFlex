package com.orange.flexoffice.meetingroomapi.ws.endPoint.data;

import static com.orange.flexoffice.meetingroomapi.ws.PathConst.SYSTEM_PATH;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines all operations available for a resource "agent".
 */
@Path(SYSTEM_PATH)
public interface SystemApiEndpoint {
	
	/**
	 * get general informations about this platform
	 * 
	 * @return If ok, a <code>System</code> with a status code 200.
	 * 
	 * @see System
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	System getSystem();

	
	// used for tests
	/*boolean executeInitTestFile();

	// used for tests
	GatewayDto findByMacAddress(String macAddress)  throws DataNotExistsException;

	// used for tests
	boolean initTeachinSensorsTable();*/

}



