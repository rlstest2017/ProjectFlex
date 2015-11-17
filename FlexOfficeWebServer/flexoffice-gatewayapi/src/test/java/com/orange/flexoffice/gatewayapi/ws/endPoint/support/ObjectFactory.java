package com.orange.flexoffice.gatewayapi.ws.endPoint.support;

import com.orange.flexoffice.gatewayapi.ws.model.EGatewayStatus;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayInput;

/**
 * ObjectFactory
 * @author oab
 *
 */
public class ObjectFactory {

	private final com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory factory = new com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory();
	
	  /**
     * Create an instance of {@link GatewayInput }
     * 
     */
    public GatewayInput createApiGateway(String status) {
    	GatewayInput gateway = factory.createGatewayInput();
    	gateway.setGatewayStatus(EGatewayStatus.valueOf(status));
        return gateway;
    }
      
}
