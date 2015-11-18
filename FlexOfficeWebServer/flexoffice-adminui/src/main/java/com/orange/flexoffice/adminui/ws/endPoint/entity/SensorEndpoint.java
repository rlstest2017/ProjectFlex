package com.orange.flexoffice.adminui.ws.endPoint.entity;


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

import static com.orange.flexoffice.adminui.ws.ParamsConst.SENSOR_IDENTIFIER_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.SENSORS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.SENSOR_UNPAIRED_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.SENSOR_IDENTIFIER_PATH;

import java.util.List;

import com.orange.flexoffice.adminui.ws.model.Sensor;
import com.orange.flexoffice.adminui.ws.model.SensorInput1;
import com.orange.flexoffice.adminui.ws.model.SensorInput2;
import com.orange.flexoffice.adminui.ws.model.SensorOutput;
import com.orange.flexoffice.adminui.ws.model.SensorSummary;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.SensorDao;

/**
 * Defines all operations available for a resource "sensor".
 */
@Path(SENSORS_PATH)
public interface SensorEndpoint {
	
	/**
	 * Gets sensors.
	 * 
	 * @return sensor summary list.
	 * 
	 * @see SensorSummary
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<SensorSummary> getSensors();
	
	/**
	 * Gets sensors.
	 * 
	 * @return unpaired sensor summary list.
	 * 
	 * @see SensorSummary
	 */
	@GET
	@Path(SENSOR_UNPAIRED_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	List<SensorSummary> getUnpairedSensors();
	
	/**
	 * Gets information on a specific sensor.
	 * 
	 * @param sensorId
	 *            the sensor ID
	 * 
	 * @return information about a specific sensor.
	 * 
	 * @see Sensor
	 */
	@GET
	@Path(SENSOR_IDENTIFIER_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Sensor getSensor(@PathParam(SENSOR_IDENTIFIER_PARAM) String sensorId);

	
	
	/**
	 * Add a new sensor.
	 * 
	 * @param sensor
	 * 			  the new sensor
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Sensor
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	SensorOutput addSensor(SensorInput1 sensor);
	
	/**
	 * Modifies a specific sensor.
	 * To identify a sensor, a sensor id is required.
	 * 
	 * @param id
	 * 			  a sensor id. 
	 * @param sensor
	 *            the new <code>sensor</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 */
	@PUT
	@Path(SENSOR_IDENTIFIER_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateSensor(@PathParam(SENSOR_IDENTIFIER_PARAM)String id, SensorInput2 sensor);

	/**
	 * Deletes a specific sensor.
	 * To identify a sensor, a sensor id is required. 
	 * 
	 * @param id
	 * 			  a sensor id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(SENSOR_IDENTIFIER_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeSensor(@PathParam(SENSOR_IDENTIFIER_PARAM)String id);

}



