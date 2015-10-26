package com.orange.flexoffice.gatewayapi.ws.endPoint.data.impl;

import java.net.URI;
import java.util.GregorianCalendar;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.gatewayapi.service.data.RelationshipManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.RelationshipEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRelationship;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class RelationshipEndpointImpl implements RelationshipEndpoint {
	
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private RelationshipManager relationshipManager;

	/**
	 * Gets a relationship.
	 * 
	 * @see RelationshipEndpoint#getRelationship(String)
	 */
	public JAXBElement<XmlRelationship> getRelationship(String id) {
		
		Relationship data = relationshipManager.find(Long.valueOf(id));
		
		if (data == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		UriBuilder ub = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");

		XmlUserRef user = factory.createXmlUserRef();
		user.setId(data.getUserId());
		user.setHref(ub.build(data.getUserId()).toString());
		
		XmlUserRef friend = factory.createXmlUserRef();
		friend.setId(data.getFriendId());
		friend.setHref(ub.build(data.getFriendId()).toString());
		
		XmlRelationship relationship = factory.createXmlRelationship();
		relationship.setId(id);
		relationship.setUser(user);
		relationship.setFriend(friend);
		relationship.setRating(data.getRating());
		relationship.setComment(data.getComment());
		
		if (data.getTimestamp() != null) {
			GregorianCalendar gCalendar = new GregorianCalendar();
			gCalendar.setTime(data.getTimestamp());
			XMLGregorianCalendar xmlCalendar;
			try {
				xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
				relationship.setTimestamp(xmlCalendar);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return factory.createRelationship(relationship);
	}

	/**
	 * Adds a relationship.
	 * 
	 * @see RelationshipEndpoint#addRelationship(XmlRelationship)
	 */
	public Response addRelationship(XmlRelationship xmlRel) throws DataAlreadyExistsException {
		Relationship rel = new Relationship();
		rel.setUserId(xmlRel.getUser().getId());
		rel.setFriendId(xmlRel.getFriend().getId());
		rel.setRating(xmlRel.getRating());
		rel.setComment(xmlRel.getComment());
		
		rel = relationshipManager.save(rel);

		URI location = uriInfo.getAbsolutePathBuilder()
				.path(RelationshipEndpoint.class, "getRelationship")
				.build(rel.getId());
		
		return Response.created(location).build();
	}

	/**
	 * Updates a relationship.
	 * 
	 * @see RelationshipEndpoint#updateRelationship(String, XmlRelationship)
	 */
	public Response updateRelationship(String id, XmlRelationship xmlRel) {
		Relationship rel = new Relationship();
		rel.setId(Long.valueOf(id));
		rel.setRating(xmlRel.getRating());
		rel.setComment(xmlRel.getComment());
		
		rel = relationshipManager.update(rel);

		URI location = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(location).build();
	}

	/**
	 * Deletes a relationship.
	 * 
	 * @see RelationshipEndpoint#removeRelationship(String)
	 */
	public Response removeRelationship(String id) {
		relationshipManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}

}
