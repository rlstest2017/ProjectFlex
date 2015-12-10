package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.data.StatEndpoint;
import com.orange.flexoffice.adminui.ws.model.MultiStatSet;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.SimpleStat;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.service.data.TestManager;

/**
 * StatEndpointImpl
 * @author oab
 *
 */
public class StatEndpointImpl implements StatEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(StatEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private TestManager testManager;
	
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<SimpleStat> getPopularStats() {
		LOGGER.info( "Begin call StatEndpoint.getPopularStats at: " + new Date() );
		
		LOGGER.info( "End call StatEndpoint.getPopularStats at: " + new Date() );
		return null;
	}

	@Override
	public List<MultiStatSet> getOccupancyStats() {
		LOGGER.info( "Begin call StatEndpoint.getOccupancyStats at: " + new Date() );
		
		LOGGER.info( "End call StatEndpoint.getOccupancyStats at: " + new Date() );
		return null;
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}
	
}
