package com.orange.flexoffice.adminui.ws.endPoint.support;

import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.dao.common.model.data.UserDao;

public class ObjectFactory {

	private final com.orange.flexoffice.adminui.ws.model.ObjectFactory factory = new com.orange.flexoffice.adminui.ws.model.ObjectFactory();
	
    /**
     * Create an instance of {@link XmlUser }
     * 
     */
    public User createHmiUser(String id, String firstName, String lastName, String email) {
    	User user = factory.createUser();
    	user.setId(id);
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
