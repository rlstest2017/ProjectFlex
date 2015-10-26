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
import com.orange.flexoffice.business.gatewayapi.service.data.LogManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.LogEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.ItemEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLog;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class LogEndpointImpl implements LogEndpoint {
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private LogManager logManager;
	
	/**
	 * Gets a log.
	 * 
	 * @see LogEndpoint#getLog(String)
	 */
	public JAXBElement<XmlLog> getLog(String id) {
		Log data = logManager.find(Long.valueOf(id));
		
		if (data == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		URI userLink = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser")
				.build(data.getUserId());
		
		URI itemLink = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem")
				.build(data.getItemId());

		XmlUserRef user = factory.createXmlUserRef();
		user.setId(data.getUserId());
		user.setHref(userLink.toString());
		
		XmlItemRef item = factory.createXmlItemRef();
		item.setId(data.getItemId());
		item.setHref(itemLink.toString());
		
		XmlLog log = factory.createXmlLog();
		log.setId(id);
		log.setUser(user);
		log.setItem(item);
		log.setRating(data.getRating());
		log.setComment(data.getComment());
		
		if(data.getTimestamp() != null) {
			GregorianCalendar gCalendar = new GregorianCalendar();
			gCalendar.setTime(data.getTimestamp());
			XMLGregorianCalendar xmlCalendar;
			try {
				xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
				log.setTimestamp(xmlCalendar);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return factory.createLog(log);
	}
	
	/**
	 * Adds a log.
	 * 
	 * @see LogEndpoint#addLog(XmlLog)
	 */
	public Response addLog(XmlLog xmlLog) throws DataAlreadyExistsException {
		Log log = new Log();
		log.setUserId(xmlLog.getUser().getId());
		log.setItemId(xmlLog.getItem().getId());
		log.setRating(xmlLog.getRating());
		log.setComment(xmlLog.getComment());
		
		log = logManager.save(log);

		URI location = uriInfo.getAbsolutePathBuilder()
				.path(LogEndpoint.class, "getLog")
				.build(log.getId());
		
		return Response.created(location).build();
	}

	/**
	 * Updates a log.
	 * @throws UnknownUniverseException 
	 * @throws SourceMissingException 
	 * 
	 * @see LogEndpoint#updateLog(String, XmlLog)
	 */
	public Response updateLog(String id, XmlLog xmlLog) { 
		Log log = new Log();
		log.setId(Long.valueOf(id));
		log.setRating(xmlLog.getRating());
		log.setComment(xmlLog.getComment());
		
		log = logManager.update(log);

		URI location = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(location).build();
	}

	/**
	 * Deletes a log.
	 * 
	 * @see LogEndpoint#removeLog(String)
	 */
	public Response removeLog(String id) {
		
		logManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}

}
