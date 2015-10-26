package com.orange.flexoffice.gatewayapi.ws.endPoint.entity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import static com.orange.flexoffice.gatewayapi.ws.PathConst.*;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.*;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristics;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptor;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreferences;

/**
 * Defines all operations available for a resource "descriptor".
 */
@Path(DESCRIPTORS_PATH)
public interface DescriptorEndpoint {
	
	/**
	 * Gets information about a specific descriptor.
	 * 
	 * @param descriptorId
	 * 			a descriptor ID
	 * @return information about a specific descriptor.
	 * 
	 * @see Descriptor
	 */
	@GET
	@Path(DESCRIPTOR_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlDescriptor> getDescriptor(@PathParam(DESCRIPTOR_ID_PARAM) String descriptorId);
	
	/**
	 * Returns a list of preferences for the specified descriptor.
	 * Allows to know all users that evaluate this descriptor.
	 * 
	 * @param descriptorId
	 * 			a descriptor ID
	 * @return a list of <code>Preference</code>
	 * 
	 * @see Preferences
	 */
	@GET
	@Path(DESCRIPTOR_ID_PATH + PREFERENCES_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlPreferences> getPreferences(@PathParam(DESCRIPTOR_ID_PARAM) String descriptorId);

	
	/**
	 * Returns a list of characteristics for the specified descriptor.
	 * Allows to know all item that are described by this descriptor.
	 * 
	 * @param descriptorId
	 * 			a descriptor ID
	 * @return a list of <code>Characteristics</code>
	 * 
	 * @see Characteristics
	 */
	@GET
	@Path(DESCRIPTOR_ID_PATH + CHARACTERISTICS_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlCharacteristics> getCharacteristics(@PathParam(DESCRIPTOR_ID_PARAM) String descriptorId);
}
