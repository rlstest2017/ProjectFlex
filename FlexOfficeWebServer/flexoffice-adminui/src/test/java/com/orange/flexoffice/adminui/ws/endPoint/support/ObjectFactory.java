package com.orange.flexoffice.adminui.ws.endPoint.support;

import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.dao.common.model.data.UserDao;

public class ObjectFactory {

	private final com.orange.flexoffice.adminui.ws.model.ObjectFactory factory = new com.orange.flexoffice.adminui.ws.model.ObjectFactory();
	
    /**
     * Create an instance of {@link XmlUser }
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
}
