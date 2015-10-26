package com.orange.flexoffice.gatewayapi.ws.endPoint.data;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.LOG_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.LOGS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.LOG_ID_PATH;

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
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLog;

/**
 * Specifies all operations available on a log resource.
 */
@Path(LOGS_PATH)
public interface LogEndpoint {

	
	/**
	 * Add a new log to the current universe.
	 * 
	 * @param log
	 * 			  the new log
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlLog
	 * @see Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	Response addLog(XmlLog log) throws DataAlreadyExistsException;
	
	
	/**
	 * Gets information on a specific log.
	 * To identify the log, a user id and an item id are required. 
	 * 
	 * @param id
	 * 			  a log id. 
	 * @return a <code>Log</code>.
	 * 
	 * @see XmlLog
	 */
	@GET
	@Path(LOG_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlLog> getLog(@PathParam(LOG_ID_PARAM)String id);

	/**
	 * Modifies a specific log.
	 * To identify a log, a user id and an item id are required.
	 * 
	 * @param id
	 * 			  a log id. 
	 * @param log
	 *            the new <code>Log</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlLog
	 * @see Response
	 */
	@PUT
	@Path(LOG_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response updateLog(@PathParam(LOG_ID_PARAM)String id, XmlLog log);

	/**
	 * Deletes a specific log.
	 * To identify a log, a user id and an item id are required. 
	 * 
	 * @param id
	 * 			  a log id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(LOG_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response removeLog(@PathParam(LOG_ID_PARAM)String id);
	
}
