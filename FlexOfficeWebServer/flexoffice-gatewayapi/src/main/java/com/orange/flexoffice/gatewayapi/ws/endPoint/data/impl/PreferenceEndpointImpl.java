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
import com.orange.flexoffice.business.gatewayapi.service.data.PreferenceManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.PreferenceEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.DescriptorEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptorRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreference;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class PreferenceEndpointImpl implements PreferenceEndpoint {

	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private PreferenceManager preferenceManager;
	
	/**
	 * Gets a preference.
	 * 
	 * @see PreferenceEndpoint#getPreference(String)
	 */
	public JAXBElement<XmlPreference> getPreference(String id) { 

		Preference data = preferenceManager.find(Long.valueOf(id));

		if (data == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		URI itemLink = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser")
				.build(data.getUserId());
		
		URI descriptorLink = uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor")
				.build(data.getDescriptorId());

		XmlUserRef user = factory.createXmlUserRef();
		user.setId(data.getUserId());
		user.setHref(itemLink.toString());
		
		XmlDescriptorRef descriptor = factory.createXmlDescriptorRef();
		descriptor.setId(data.getDescriptorId());
		descriptor.setHref(descriptorLink.toString());
		
		XmlPreference p = factory.createXmlPreference();
		p.setId(id);
		p.setDescriptor(descriptor);
		p.setUser(user);
		p.setRating(data.getRating());
		p.setComment(data.getComment());
		
		if (data.getTimestamp() != null) {
			GregorianCalendar gCalendar = new GregorianCalendar();
			gCalendar.setTime(data.getTimestamp());
			XMLGregorianCalendar xmlCalendar;
			try {
				xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
				p.setTimestamp(xmlCalendar);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return factory.createPreference(p);
	}

	/**
	 * Adds a preference.
	 * 
	 * @see PreferenceEndpoint#addPreference(XmlPreference)
	 */
	public Response addPreference(XmlPreference xmlPreference) throws DataAlreadyExistsException {
		Preference pref = new Preference();
		pref.setUserId(xmlPreference.getUser().getId());
		pref.setDescriptorId(xmlPreference.getDescriptor().getId());
		pref.setRating(xmlPreference.getRating());
		pref.setComment(xmlPreference.getComment());
		
		pref = preferenceManager.save(pref);

		URI location = uriInfo.getAbsolutePathBuilder()
				.path(PreferenceEndpoint.class, "getPreference")
				.build(pref.getId());
		
		return Response.created(location).build();
	}

	/**
	 * Updates a preference.
	 * 
	 * @see PreferenceEndpoint#updatePreference(String, XmlPreference)
	 */
	public Response updatePreference(String id, XmlPreference xmlPreference) {
		Preference pref = new Preference();
		pref.setId(Long.valueOf(id));
		pref.setRating(xmlPreference.getRating());
		pref.setComment(xmlPreference.getComment());
		
		pref = preferenceManager.update(pref);
		
		URI location = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(location).build();
	}

	/**
	 * Removes a preference.
	 * 
	 * @see PreferenceEndpoint#removePreference(String)
	 */
	public Response removePreference(String id) {
		preferenceManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}

}
