package com.orange.flexoffice.adminui.ws.endPoint.support;

import com.orange.flexoffice.adminui.ws.model.GatewayInput;
import com.orange.flexoffice.adminui.ws.model.GatewayInput3;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.dao.common.model.data.UserDao;

public class ObjectFactory {

	private final com.orange.flexoffice.adminui.ws.model.ObjectFactory factory = new com.orange.flexoffice.adminui.ws.model.ObjectFactory();
	
    /**
     * Create an instance of {@link UserInput }
     * 
     */
    public UserInput createHmiUser(String firstName, String lastName, String email) {
    	UserInput user = factory.createUserInput();
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	
        return user;
    }
  
    /**
     * Create an instance of {@link UserDao }
     * 
     */
    public UserDao createUser(Long id, String firstName, String lastName, String email) {
    	UserDao user = new UserDao();
    	user.setId(id);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	
        return user;
    }
    
    /**
     * Create an instance of {@link GatewayInput3 }
     * 
     */
    public GatewayInput3 createHmiGateway(String macAddress, String name, String desc) {
    	GatewayInput3 gateway = factory.createGatewayInput3();
    	gateway.setMacAddress(macAddress);
    	gateway.setName(name);
    	gateway.setDesc(desc);
    	
        return gateway;
    }
    
    /**
     * Create an instance of {@link GatewayInput3 }
     * 
     */
    public GatewayInput createHmi2Gateway(String name, String desc) {
    	GatewayInput gateway = factory.createGatewayInput();
    	gateway.setName(name);
    	gateway.setDesc(desc);
    	
        return gateway;
    }
}
