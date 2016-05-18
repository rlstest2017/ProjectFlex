package com.orange.flexoffice.meetingroomapi.ws.endPoint.data.impl;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.meetingroomapi.ws.endPoint.data.SystemApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.ObjectFactory;
import com.orange.flexoffice.meetingroomapi.ws.model.System;

/**
 * SystemApiEndpointImpl
 * @author oab
 *
 */
public class SystemApiEndpointImpl implements SystemApiEndpoint {

	private static final Logger LOGGER = Logger.getLogger(SystemApiEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private SystemManager systemManager;
	@Autowired
	private TestManager testManager;

	@Override
	public System getSystem() {
		LOGGER.info("SystemApiEndpointImpl.getSystem");
		
		System system = factory.createSystem();
		ConfigurationDao configuration;
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.AGENT_STATUS_TIMEOUT.toString());
		if (configuration != null)
		system.setAgentTimeout(BigInteger.valueOf(Long.valueOf(configuration.getValue())));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.DASHBOARD_STATUS_TIMEOUT.toString());
		if (configuration != null)
		system.setDashboardTimeout(BigInteger.valueOf(Long.valueOf(configuration.getValue())));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.MEETINGROOM_STATUS_TIMEOUT.toString());
		if (configuration != null)
		system.setMeetingRoomTimeout(BigInteger.valueOf(Long.valueOf(configuration.getValue())));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.WS_REFRESH_INTERVAL.toString());
		if (configuration != null)
		system.setWsRefreshInterval(Long.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.INACTIVITY_TIME.toString());
		if (configuration != null)
		system.setInactivityTime(Long.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.HOUR_START.toString());
		if (configuration != null)
		system.setHourStart(Long.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.HOUR_END.toString());
		if (configuration != null)
		system.setHourEnd(Long.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.ORGANIZER_MANDATORY.toString());
		if (configuration != null)
		system.setOrganizerMandatory(Boolean.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.SUBJECT_MANDATORY.toString());
		if (configuration != null)
		system.setSubjectMandatory(Boolean.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.ACK_TIME.toString());
		if (configuration != null)
		system.setAckTime(Long.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.USER_CAN_CANCEL.toString());
		if (configuration != null)
		system.setUserCanCancel(Boolean.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.CAN_SHOW_SUBJECT.toString());
		if (configuration != null)
		system.setCanShowSubject(Boolean.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.CAN_SHOW_ORGANIZER.toString());
		if (configuration != null)
		system.setCanShowOrganizer(Boolean.valueOf(configuration.getValue()));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.DURATION_STEP.toString());
		if (configuration != null)
		system.setDurationStep(BigInteger.valueOf(Long.valueOf(configuration.getValue())));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.PAGES_SHIFT_INTERVAL.toString());
		if (configuration != null)
		system.setPagesShiftInterval(BigInteger.valueOf(Long.valueOf(configuration.getValue())));
		
		configuration = systemManager.getConfigurationValue(E_ConfigurationKey.NB_ROOMS_PER_PAGE.toString());
		if (configuration != null)
		system.setNbRoomsPerPage(BigInteger.valueOf(Long.valueOf(configuration.getValue())));
		
		return factory.createSystem(system).getValue();
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

}
