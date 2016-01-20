package com.orange.flexoffice.adminui.ws.utils;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.service.data.GatewayManager;


/**
 * InitOfflineGatewaysAlerts
 * @author oab
 *
 */
public class InitOfflineGatewaysAlerts {
    
	private static final Logger LOGGER = Logger.getLogger(InitOfflineGatewaysAlerts.class);
	
	@Autowired
	private GatewayManager gatewayManager;
	
	/**
	 * updateOFFLINEGatewaysAlerts
	 */
	public void updateOFFLINEGatewaysAlerts() {
		LOGGER.debug("Begin InitOfflineGatewaysAlerts.updateOFFLINEGatewaysAlerts method :" + new Date());
		gatewayManager.updateOFFLINEGatewaysAlerts();
		LOGGER.debug("End InitOfflineGatewaysAlerts.updateOFFLINEGatewaysAlerts method :" + new Date());
	}

	// for Test
	public Boolean updateOFFLINEGatewaysAlertsForTest() {
		LOGGER.debug("Begin updateOFFLINEGatewaysAlertsForTest.updateOFFLINEGatewaysAlerts method :" + new Date());
		gatewayManager.updateOFFLINEGatewaysAlerts();
		LOGGER.debug("End updateOFFLINEGatewaysAlertsForTest.updateOFFLINEGatewaysAlerts method :" + new Date());
		return true;
	}

}
