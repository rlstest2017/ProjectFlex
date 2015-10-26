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

import com.orange.flexoffice.business.gatewayapi.service.object.DescriptorManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.object.Descriptor;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.CharacteristicEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.PreferenceEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.DescriptorEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.ItemEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristic;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristicRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristics;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptor;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptorRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreference;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreferenceRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreferences;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class DescriptorEndpointImpl implements DescriptorEndpoint {
	private final Logger logger = Logger.getLogger(DescriptorEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private DescriptorManager descriptorManager;
	
	/**
	 * Get a descriptor.
	 * 
	 * @see DescriptorEndpoint#getDescriptor(String, String)
	 */
	public JAXBElement<XmlDescriptor> getDescriptor(String descriptorId) {
		Descriptor descriptor = descriptorManager.find(descriptorId);
		
		XmlDescriptor xmlDescriptor = factory.createXmlDescriptor();
		xmlDescriptor.setId(descriptorId);
		xmlDescriptor.setCharacteristics(factory.createXmlDescriptorCharacteristics());
		xmlDescriptor.setPreferences(factory.createXmlDescriptorPreferences());
		
		UriBuilder ub = null;
		
		// Collect preferences
		if (descriptor.getPreferences() != null && !descriptor.getPreferences().isEmpty()) {
			ub =  uriInfo.getBaseUriBuilder()
					.path(PreferenceEndpoint.class)
					.path(PreferenceEndpoint.class, "getPreference");
			
			for (Preference pref : descriptor.getPreferences()) {
				XmlPreferenceRef pr = factory.createXmlPreferenceRef();
				pr.setId(pref.getId().toString());
				pr.setHref(ub.build(pr.getId()).toString());
				xmlDescriptor.getPreferences().getPreference().add(pr);
			}
		}
		
		// Collect characteristics
		if (descriptor.getCharacteristics() != null && !descriptor.getCharacteristics().isEmpty()) {
			ub =  uriInfo.getBaseUriBuilder()
					.path(CharacteristicEndpoint.class)
					.path(CharacteristicEndpoint.class, "getCharacteristic");
			
			for (Characteristic charac : descriptor.getCharacteristics()) {
				XmlCharacteristicRef cr = factory.createXmlCharacteristicRef();
				cr.setId(charac.getId().toString());
				cr.setHref(ub.build(cr.getId()).toString());
				xmlDescriptor.getCharacteristics().getCharacteristic().add(cr);
			}
		}
		
		return factory.createDescriptor(xmlDescriptor);
	}
	
	/**
	 * Returns a list of preferences concerning a descriptor.
	 * 
	 * @see DescriptorEndpoint#getPreferences(String, String)
	 */
	public JAXBElement<XmlPreferences> getPreferences(String descriptorId) {
		List<Preference> preferences = descriptorManager.getDescriptorPreference(descriptorId);
		XmlPreferences xmlPreferences = factory.createXmlPreferences();
		
		if (preferences == null || preferences.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No preferences registred for the descriptor[" + descriptorId +"]");
			}
			return factory.createPreferences(xmlPreferences);
		}
		
		UriBuilder userUb = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");
		
		UriBuilder descUb = uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor");
		
		XmlDescriptorRef xmlDescriptor = factory.createXmlDescriptorRef();
		xmlDescriptor.setId(descriptorId);
		xmlDescriptor.setHref(descUb.build(descriptorId).toString());
		
		// Computes preferences
		for (Preference pref : preferences) {
			XmlUserRef xmlUser = factory.createXmlUserRef();
			xmlUser.setId(pref.getUserId());
			xmlUser.setHref(userUb.build(xmlUser.getId()).toString());
			
			XmlPreference xmlPref = factory.createXmlPreference();
			xmlPref.setId(pref.getId().toString());
			xmlPref.setUser(xmlUser);
			xmlPref.setDescriptor(xmlDescriptor);
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
	 * Returns a list of characteristics concerning a descriptor.
	 * 
	 * @see DescriptorEndpoint#getCharacteristics(String, String)
	 */
	public JAXBElement<XmlCharacteristics> getCharacteristics(String descriptorId) {
		List<Characteristic> characteristics = descriptorManager.getDescriptorCharacteristic(descriptorId);
		XmlCharacteristics xmlCharacteristics = factory.createXmlCharacteristics();
		
		if (characteristics == null || characteristics.isEmpty()){
			if (logger.isInfoEnabled()) {
				logger.info("No characteristics registered for '" + descriptorId + "'");
			}
			return factory.createCharacteristics(xmlCharacteristics);
		}
		
		UriBuilder itemUb = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem");
		
		UriBuilder descUb =  uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor");
		
		XmlDescriptorRef xmlDescriptor = factory.createXmlDescriptorRef();
		xmlDescriptor.setId(descriptorId);
		xmlDescriptor.setHref(descUb.build(descriptorId).toString());
		
		// Computes characteristics
		for (Characteristic charac : characteristics) {
			XmlItemRef xmlItem = factory.createXmlItemRef();
			xmlItem.setId(charac.getItemId());
			xmlItem.setHref(itemUb.build(xmlItem.getId()).toString());
			
			XmlCharacteristic xmlCharac = factory.createXmlCharacteristic();
			xmlCharac.setId(charac.getId().toString());
			xmlCharac.setDescriptor(xmlDescriptor);
			xmlCharac.setItem(xmlItem);
			xmlCharac.setWeight(charac.getWeight());
			xmlCharac.setComment(charac.getComment());
			
			if(charac.getTimestamp() != null) {
				GregorianCalendar gCalendar = new GregorianCalendar();
				gCalendar.setTime(charac.getTimestamp());
				XMLGregorianCalendar xmlCalendar;
				try {
					xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
					xmlCharac.setTimestamp(xmlCalendar);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			xmlCharacteristics.getCharacteristic().add(xmlCharac);
		}
		
		return factory.createCharacteristics(xmlCharacteristics);
	}

}
