package com.orange.flexoffice.gatewayapi.ws.endPoint.data;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.CHARACTERISTIC_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.CHARACTERISTICS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.CHARACTERISTIC_ID_PATH;

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
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristic;

/**
 * Specifies all operations available on the characteristic resource.
 * 
 * Each characteristic is identified thanks a item ID, a descriptor Type and a descriptor Value.
 */
@Path(CHARACTERISTICS_PATH)
public interface CharacteristicEndpoint {
	
	/**
	 * Gets information on a specific characteristic.
	 * 
	 * @param id
	 * 		  a characteristic ID
	 * 
	 * @return a {@link XmlCharacteristic}
	 */
	@GET
	@Path(CHARACTERISTIC_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlCharacteristic> getCharacteristic(@PathParam(CHARACTERISTIC_ID_PARAM)String id);
	
	/**
	 * Creates a characteristic.
	 * 
	 * @param characteristic
	 * 		  a characteristic
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 * @see XmlCharacteristic
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	Response addCharacteristic(XmlCharacteristic characteristic) throws DataAlreadyExistsException;
	
	/**
	 * Modifies a characteristic.
	 * 
	 * @param id
	 * 		  a characteristic ID
	 * @param characteristic
	 * 		  the new characteristic
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 */
	@PUT
	@Path(CHARACTERISTIC_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response updateCharacteristic(
			@PathParam(CHARACTERISTIC_ID_PARAM)String id,
			XmlCharacteristic characteristic);
	
	/**
	 * Deletes a characteristic.
	 * 
	 * @param id
	 * 		  a characteristic ID
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 */
	@DELETE
	@Path(CHARACTERISTIC_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response removeCharacteristic(@PathParam(CHARACTERISTIC_ID_PARAM)String id);
}
