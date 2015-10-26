package com.orange.flexoffice.gatewayapi.ws.endPoint.data.impl;

import java.net.URI;
import java.util.GregorianCalendar;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.gatewayapi.service.data.CharacteristicManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.CharacteristicEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.DescriptorEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.ItemEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristic;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptorRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;

public class CharacteristicEndpointImpl implements CharacteristicEndpoint {

	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private CharacteristicManager characteristicManager;
	
	/**
	 * Gets a characteristic.
	 * 
	 * @see CharacteristicEndpoint#getCharacteristic(String)
	 */
	public JAXBElement<XmlCharacteristic> getCharacteristic(String id) {
		Characteristic result = 
				characteristicManager.find(Long.valueOf(id));
		
		if (result == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		URI itemLink = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem")
				.build(result.getItemId());
		
		URI descriptorLink = uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor")
				.build(result.getDescriptorId());

		XmlItemRef item = factory.createXmlItemRef();
		item.setId(result.getItemId());
		item.setHref(itemLink.toString());
		
		XmlDescriptorRef descriptor = factory.createXmlDescriptorRef();
		descriptor.setId(result.getDescriptorId());
		descriptor.setHref(descriptorLink.toString());
		
		XmlCharacteristic c = factory.createXmlCharacteristic();
		c.setId(id);
		c.setDescriptor(descriptor);
		c.setItem(item);
		c.setWeight(result.getWeight());
		c.setComment(result.getComment());
		
		if (result.getTimestamp() != null) {
			GregorianCalendar gCalendar = new GregorianCalendar();
			gCalendar.setTime(result.getTimestamp());
			XMLGregorianCalendar xmlCalendar;
			try {
				xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
				c.setTimestamp(xmlCalendar);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return factory.createCharacteristic(c);
	}

	/**
	 * Adds a characteristic.
	 * 
	 * @see CharacteristicEndpoint#addCharacteristic(XmlCharacteristic)
	 */
	public Response addCharacteristic(XmlCharacteristic xmlCharac) throws DataAlreadyExistsException {
		Characteristic c = new Characteristic();
		c.setItemId(xmlCharac.getItem().getId());
		c.setDescriptorId(xmlCharac.getDescriptor().getId());
		c.setWeight(xmlCharac.getWeight());
		c.setComment(xmlCharac.getComment());
		
		c = characteristicManager.save(c);

		URI location = uriInfo.getAbsolutePathBuilder()
				.path(CharacteristicEndpoint.class, "getCharacteristic")
				.build(c.getId());
		
		return Response.created(location).build();
	}

	/**
	 * Updates a characteristics.
	 * 
	 * @see CharacteristicEndpoint#updateCharacteristic(String, XmlCharacteristic)
	 */
	public Response updateCharacteristic(String id, XmlCharacteristic xmlCharac) {
		Characteristic c = new Characteristic();
		c.setId(Long.valueOf(id));
		c.setWeight(xmlCharac.getWeight());
		c.setComment(xmlCharac.getComment());
        
		c = characteristicManager.update(c);

		URI location = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(location).build();
	}

	/**
	 * Removes a characteristic.
	 * @throws UnknownUniverseException 
	 * @throws SourceMissingException 
	 * 
	 * @see CharacteristicEndpoint#removeCharacteristic(String)
	 */
	public Response removeCharacteristic(String id) { 
		characteristicManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}

}
