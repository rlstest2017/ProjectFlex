package com.orange.flexoffice.gatewayapi.ws.endPoint.entity;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.USER_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.LOGS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.USERS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.USER_ID_PATH;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.gatewayapi.ws.model.XmlLogs;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUser;

/**
 * Defines all operations available for a resource "user".
 */
@Path(USERS_PATH)
public interface UserEndpoint {

	/**
	 * Gets information on a specific user.
	 * 
	 * @param userId
	 *            the user ID
	 * 
	 * @return information about a specific user.
	 * 
	 * @see User
	 */
	@GET
	@Path(USER_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlUser> getUser(@PathParam(USER_ID_PARAM) String userId);

	/**
	 * Returns the list of the logs from a user's profile.
	 * 
	 * @param userId
	 *            a user ID
	 * @return a collection of <code>Log</code>
	 * 
	 * @see Logs
	 */
	@GET
	@Path(USER_ID_PATH + LOGS_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlLogs> getLogs(@PathParam(USER_ID_PARAM) String userId);
	
}
