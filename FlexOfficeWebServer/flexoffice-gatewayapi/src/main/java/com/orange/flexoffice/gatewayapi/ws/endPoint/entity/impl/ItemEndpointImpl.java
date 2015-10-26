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

import com.orange.flexoffice.business.gatewayapi.service.object.ItemManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.object.Item;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.CharacteristicEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.data.LogEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.DescriptorEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.ItemEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristic;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristicRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlCharacteristics;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptorRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItem;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLog;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLogRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLogs;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class ItemEndpointImpl implements ItemEndpoint {
	private final Logger logger = Logger.getLogger(ItemEndpoint.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private ItemManager itemManager;
	
	/**
	 * Get a specific item.
	 * 
	 * @see ItemEndpoint#getItem(String)
	 */
	public JAXBElement<XmlItem> getItem(String itemId) {
		Item item = itemManager.find(itemId);
				
		XmlItem xmlItem = factory.createXmlItem();
		xmlItem.setId(itemId);
		xmlItem.setCharacteristics(factory.createXmlItemCharacteristics());
		xmlItem.setLogs(factory.createXmlItemLogs());
		
		UriBuilder ub =  null;
		
		// Collect characteristics
		if (item.getLogs() != null && !item.getLogs().isEmpty()) {
			ub =  uriInfo.getBaseUriBuilder()
					.path(CharacteristicEndpoint.class)
					.path(CharacteristicEndpoint.class, "getCharacteristic");
			
			for (Characteristic charac : item.getCharacteristics()) {
				XmlCharacteristicRef cr = factory.createXmlCharacteristicRef();
				cr.setId(charac.getId().toString());
				cr.setHref(ub.build(cr.getId()).toString());
				xmlItem.getCharacteristics().getCharacteristic().add(cr);
			}
		}
		
		// Collect logs
		if (item.getCharacteristics() != null && !item.getCharacteristics().isEmpty()) {
			ub = uriInfo.getBaseUriBuilder()
					.path(LogEndpoint.class)
					.path(LogEndpoint.class, "getLog");
			
			for (Log log : item.getLogs()) {
				XmlLogRef lr = factory.createXmlLogRef();
				lr.setId(log.getId().toString());
				lr.setHref(ub.build(lr.getId()).toString());
				xmlItem.getLogs().getLog().add(lr);
			}
		}
		
		return factory.createItem(xmlItem);
	}
	
	/**
	 * Returns the list of logs of an item.
	 * 
	 * @see ItemEndpoint#getLogs(String)
	 */
	public JAXBElement<XmlLogs> getLogs(String itemId) {
		List<Log> logs = itemManager.getItemLogs(itemId);
		
		XmlLogs xmlLogs = factory.createXmlLogs();
		
		if (logs == null || logs.isEmpty()){
			if (logger.isInfoEnabled()) {
				logger.info("No logs registered for '" + itemId + "'");
			}
			return factory.createLogs(xmlLogs);
		}
		
		UriBuilder userUb = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");
		
		UriBuilder itemUb = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem");
		
		XmlItemRef itemRef = factory.createXmlItemRef();
		itemRef.setId(itemId);
		itemRef.setHref(itemUb.build(itemId).toString());
		
		for (Log log : logs) {
			XmlUserRef userRef = factory.createXmlUserRef();
			userRef.setId(log.getUserId());
			userRef.setHref(userUb.build(userRef.getId()).toString());
			
			XmlLog xmlLog = factory.createXmlLog();
			xmlLog.setId(log.getId().toString());
			xmlLog.setItem(itemRef);
			xmlLog.setUser(userRef);
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
	 * Returns the list of descriptors of an item.
	 * 
	 * @see ItemEndpoint#getDescriptors(String)
	 */
	public JAXBElement<XmlCharacteristics> getCharacteristics(String itemId) {
		List<Characteristic> characteristics = itemManager.getItemCharacteristics(itemId);
		
		XmlCharacteristics xmlCharacteristics = factory.createXmlCharacteristics();
		
		if (characteristics == null || characteristics.isEmpty()){
			if (logger.isInfoEnabled()) {
				logger.info("No descriptors registered for '" + itemId + "'");
			}
			return factory.createCharacteristics(xmlCharacteristics);
		}
		
		UriBuilder itemUb = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem");
		
		UriBuilder descriptorUb =  uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor");

		XmlItemRef itemRef = factory.createXmlItemRef();
		itemRef.setId(itemId);
		itemRef.setHref(itemUb.build(itemId).toString());
		
		for (Characteristic charac : characteristics) {
			XmlDescriptorRef descRef = factory.createXmlDescriptorRef();
			descRef.setId(charac.getDescriptorId());
			descRef.setHref(descriptorUb.build(descRef.getId()).toString());
			
			XmlCharacteristic xmlCharac = factory.createXmlCharacteristic();
			xmlCharac.setId(charac.getId().toString());
			xmlCharac.setDescriptor(descRef);
			xmlCharac.setItem(itemRef);
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
