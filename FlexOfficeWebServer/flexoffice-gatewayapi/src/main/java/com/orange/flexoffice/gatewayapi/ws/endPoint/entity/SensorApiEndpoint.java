package com.orange.flexoffice.gatewayapi.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.orange.flexoffice.gatewayapi.ws.model.SensorInput;
import com.orange.flexoffice.gatewayapi.ws.model.SensorNewSummary;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.SENSOR_IDENTIFIER_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.SENSORS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.SENSOR_ID_PATH;



/**
 * Defines all operations available for a resource "sensor".
 */
@Path(SENSORS_PATH)
public interface SensorApiEndpoint {
	
	
	/**
	 * Update sensor information.
	 * 
	 * @param id
	 * 			  a sensor id. 
	 * @param sensor
	 *            the updated <code>sensor</code>
	 *            
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response addSensor(SensorNewSummary sensor);

	
	/**
	 * Update sensor information.
	 * 
	 * @param id
	 * 			  a sensor id. 
	 * @param sensor
	 *            the updated <code>sensor</code>
	 *            
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 */
	@PUT
	@Path(SENSOR_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateSensor(@PathParam(SENSOR_IDENTIFIER_PARAM)String identifier, SensorInput sensor);

}



