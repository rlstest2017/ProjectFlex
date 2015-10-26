package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.service.object.UserManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;
import com.orange.flexoffice.dao.gatewayapi.model.object.User;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.LogEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.PreferenceEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.RelationshipEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.DescriptorEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.ItemEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptorRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLog;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLogRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLogs;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreference;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreferenceRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreferences;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRelationship;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRelationshipRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRelationships;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUser;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class UserEndpointImpl implements UserEndpoint {
	
	private final Logger logger = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private UserManager userManager;
	
	/**
	 * Get a specific user.
	 * 
	 * @see UserEndpoint#getUser(String)
	 */
	public JAXBElement<XmlUser> getUser(String userId) {
		XmlUser xmlUser = factory.createXmlUser();
		xmlUser.setId(userId);
		xmlUser.setLogs(factory.createXmlUserLogs());
		xmlUser.setPreferences(factory.createXmlUserPreferences());
		xmlUser.setFriends(factory.createXmlUserFriends());
		xmlUser.setFollowers(factory.createXmlUserFollowers());
		
		User user = userManager.find(userId);
		
		UriBuilder ub = null;
		if (user.getLogs() != null && !user.getLogs().isEmpty()) {
			// Collect logs
			ub = uriInfo
					.getBaseUriBuilder()
					.path(LogEndpoint.class)
					.path(LogEndpoint.class, "getLog");
			
			for (Log log : user.getLogs()) {
				XmlLogRef lr = factory.createXmlLogRef();
				lr.setId(log.getId().toString());
				lr.setHref(ub.build(lr.getId()).toString());
				xmlUser.getLogs().getLog().add(lr);
			}
		}
		
		// Collect friends and followers
		
		if (user.getFriends() != null && !user.getFriends().isEmpty()) {
			ub =  uriInfo
					.getBaseUriBuilder()
					.path(RelationshipEndpoint.class)
					.path(RelationshipEndpoint.class, "getRelationship");
			
			for (Relationship rel : user.getFriends()) {
				XmlRelationshipRef rr = factory.createXmlRelationshipRef();
				rr.setId(rel.getId().toString());
				rr.setHref(ub.build(rr.getId()).toString());
				xmlUser.getFriends().getFriend().add(rr);
			}
		}
		
		if (user.getFollowers() != null && !user.getFollowers().isEmpty()) {
			ub =  uriInfo
					.getBaseUriBuilder()
					.path(RelationshipEndpoint.class)
					.path(RelationshipEndpoint.class, "getRelationship");
			
			for (Relationship rel : user.getFollowers()) {
				XmlRelationshipRef rr = factory.createXmlRelationshipRef();
				rr.setId(rel.getId().toString());
				rr.setHref(ub.build(rr.getId()).toString());
				xmlUser.getFollowers().getFollower().add(rr);
			}
		}
		
		// Collect preferences

		if (user.getPreferences() != null && !user.getPreferences().isEmpty()) {
			ub =  uriInfo
					.getBaseUriBuilder()
					.path(PreferenceEndpoint.class)
					.path(PreferenceEndpoint.class, "getPreference");
			
			for (Preference pref : user.getPreferences()) {
				XmlPreferenceRef pr = factory.createXmlPreferenceRef();
				pr.setId(pref.getId().toString());
				pr.setHref(ub.build(pr.getId()).toString());
				xmlUser.getPreferences().getPreference().add(pr);
			}
		}
		
		return factory.createUser(xmlUser);
	}
	
	/**
	 * Returns logs register in a user's profile.
	 * 
	 * @see UserEndpoint#getLogs(String)
	 */
	public JAXBElement<XmlLogs> getLogs(String userId) {
		List<Log> logs = userManager.getUserLogs(userId);
		
		XmlLogs xmlLogs = factory.createXmlLogs();

		if (logs == null || logs.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No log registered for '" + userId + "'");
			}
			return factory.createLogs(xmlLogs);
		}

		UriBuilder userUb = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");
		
		UriBuilder itemUb = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem");
		
		XmlUserRef userRef = factory.createXmlUserRef();
		userRef.setId(userId);
		userRef.setHref(userUb.build(userId).toString());
		
		for (Log log : logs) {
			XmlItemRef itemRef = factory.createXmlItemRef();
			itemRef.setId(log.getItemId());
			itemRef.setHref(itemUb.build(log.getItemId()).toString());
			
			XmlLog xmlLog = factory.createXmlLog();
			xmlLog.setId(log.getId().toString());
			xmlLog.setUser(userRef);
			xmlLog.setItem(itemRef);
			xmlLog.setRating(log.getRating());
			xmlLog.setComment(log.getComment());
			
			if(log.getTimestamp() != null) {
				GregorianCalendar gCalendar = new GregorianCalendar();
				gCalendar.setTime(log.getTimestamp());
				XMLGregorianCalendar xmlCalendar;
				try {
					xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
					xmlLog.setTimestamp(xmlCalendar);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			xmlLogs.getLog().add(xmlLog);
		}
		return factory.createLogs(xmlLogs);
	}

	/**
	 * Returns preferences register in a user's profile.
	 * 
	 * @see UserEndpoint#getPreferences(String)
	 */
	public JAXBElement<XmlPreferences> getPreferences(String userId) {
		List<Preference> preferences = userManager.getUserPreferences(userId);
		
		XmlPreferences xmlPreferences = factory.createXmlPreferences();
		
		if (preferences == null || preferences.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No preference registered for '" + userId + "'");
			}
			return factory.createPreferences(xmlPreferences);
		}
		
		UriBuilder userUb = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");
		
		UriBuilder descUb = uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor");


		XmlUserRef userRef = factory.createXmlUserRef();
		userRef.setId(userId);
		userRef.setHref(userUb.build(userId).toString());
		
		for (Preference pref : preferences) {
			XmlDescriptorRef descRef = factory.createXmlDescriptorRef();
			descRef.setId(pref.getDescriptorId());
			descRef.setHref(descUb.build(pref.getDescriptorId()).toString());
			
			XmlPreference xmlPref = factory.createXmlPreference();
			xmlPref.setId(pref.getId().toString());
			xmlPref.setUser(userRef);
			xmlPref.setDescriptor(descRef);
			xmlPref.setRating(pref.getRating());
			xmlPref.setComment(pref.getComment());
			
			if(pref.getTimestamp() != null) {
				GregorianCalendar gCalendar = new GregorianCalendar();
				gCalendar.setTime(pref.getTimestamp());
				XMLGregorianCalendar xmlCalendar;
				try {
					xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
					xmlPref.setTimestamp(xmlCalendar);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			xmlPreferences.getPreference().add(xmlPref);
		}
		
		return factory.createPreferences(xmlPreferences);
	}

	/**
	 * Returns friends register in the user's profile.
	 * 
	 * @see UserEndpoint#getFriends(String)
	 */
	public JAXBElement<XmlRelationships> getFriends(String userId) {
		List<Relationship> relationships = userManager.getUserFriends(userId);
		
		XmlRelationships xmlRelationships = factory.createXmlRelationships();
		
		if (relationships == null || relationships.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No friend registered for '" + userId + "'");
			}
			return factory.createRelationships(xmlRelationships);
		}
		
		UriBuilder userUb = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");

		XmlUserRef userRef = factory.createXmlUserRef();
		userRef.setId(userId);
		userRef.setHref(userUb.build(userId).toString());

		for (Relationship rel : relationships) {
			XmlUserRef friendRef = factory.createXmlUserRef();
			friendRef.setId(rel.getFriendId());
			friendRef.setHref(userUb.build(rel.getFriendId()).toString());
			
			XmlRelationship xmlRel = factory.createXmlRelationship();
			xmlRel.setId(rel.getId().toString());
			xmlRel.setUser(userRef);
			xmlRel.setFriend(friendRef);
			xmlRel.setRating(rel.getRating());
			xmlRel.setComment(rel.getComment());
			
			if(rel.getTimestamp() != null) {
				GregorianCalendar gCalendar = new GregorianCalendar();
				gCalendar.setTime(rel.getTimestamp());
				XMLGregorianCalendar xmlCalendar;
				try {
					xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
					xmlRel.setTimestamp(xmlCalendar);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			xmlRelationships.getRelationship().add(xmlRel);
		}

		return factory.createRelationships(xmlRelationships);
	}
	
	/**
	 * Returns the list of users who rated the current user.
	 * 
	 * @see UserEndpoint#getFollowers(String)
	 */
	public JAXBElement<XmlRelationships> getFollowers(String userId) {
		List<Relationship> relationships = userManager.getUserFollowers(userId);
		
		XmlRelationships xmlRelationships = factory.createXmlRelationships();
		
		if (relationships == null || relationships.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No friend registered for '" + userId + "'");
			}
			return factory.createRelationships(xmlRelationships);
		}
		
		UriBuilder userUb = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");

		XmlUserRef friendRef = factory.createXmlUserRef();
		friendRef.setId(userId);
		friendRef.setHref(userUb.build(userId).toString());

		for (Relationship rel : relationships) {
			XmlUserRef userRef = factory.createXmlUserRef();
			userRef.setId(rel.getUserId());
			userRef.setHref(userUb.build(rel.getUserId()).toString());
			
			XmlRelationship xmlRel = factory.createXmlRelationship();
			xmlRel.setId(rel.getId().toString());
			xmlRel.setUser(userRef);
			xmlRel.setFriend(friendRef);
			xmlRel.setRating(rel.getRating());
			xmlRel.setComment(rel.getComment());
			
			if(rel.getTimestamp() != null) {
				GregorianCalendar gCalendar = new GregorianCalendar();
				gCalendar.setTime(rel.getTimestamp());
				XMLGregorianCalendar xmlCalendar;
				try {
					xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
					xmlRel.setTimestamp(xmlCalendar);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			xmlRelationships.getRelationship().add(xmlRel);
		}

		return factory.createRelationships(xmlRelationships);
	}

}
