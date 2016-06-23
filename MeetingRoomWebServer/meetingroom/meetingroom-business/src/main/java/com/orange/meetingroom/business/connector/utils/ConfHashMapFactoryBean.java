package com.orange.meetingroom.business.connector.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.enums.EnumSystemInMap;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;

/**
 * ConfHashMapFactoryBean
 * @author oab
 *
 */
@Component
public class ConfHashMapFactoryBean implements FactoryBean<Map<String, Integer>> {
	
	private static final Logger LOGGER = Logger.getLogger(ConfHashMapFactoryBean.class);
	@Autowired
	FlexOfficeConnectorClient flexofficeConnector;
	@Autowired
	DateTools dateTools;
	Map<String, Integer> configMap = null;

	/**
	 * initialize method, get params from DB
	 */
	public void initialize() {
		try {
			configMap = new HashMap<String, Integer>();
			SystemConnectorReturn systemreturn;
			systemreturn = flexofficeConnector.getSystem();
			Integer nbSeconds = systemreturn.getDashboardStartDate();

			configMap.put(EnumSystemInMap.ACK_TIME.toString(), systemreturn.getAckTime());
			configMap.put(EnumSystemInMap.DASHBOARD_START_DATE.toString(), nbSeconds);
			configMap.put(EnumSystemInMap.DASHBOARD_MAX_BOOKINGS.toString(), systemreturn.getDashboardMaxBookings());
			configMap.put(EnumSystemInMap.HOUR_START.toString(), systemreturn.getHourStart());
			configMap.put(EnumSystemInMap.HOUR_END.toString(), systemreturn.getHourEnd());
			
		} catch (FlexOfficeInternalServerException | MeetingRoomInternalServerException e) {
			LOGGER.debug("Exception thrown in flexofficeConnector.getSystem() in ConfHashMapFactoryBean class: " + e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String, Integer> getObject()  {
		if (configMap == null) {
			initialize();
		}
		return configMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Map> getObjectType() {
		return Map.class ;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
