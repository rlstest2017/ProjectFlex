package com.orange.flexoffice.userui.ws.endPoint.support;

import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;
import com.orange.flexoffice.userui.ws.model.XmlUser;

public class ObjectFactory {

	private final com.orange.flexoffice.userui.ws.model.ObjectFactory factory = new com.orange.flexoffice.userui.ws.model.ObjectFactory();
	
    /**
     * Create an instance of {@link XmlUser }
     * 
     */
    public XmlUser createXmlUser(String id, String firstName, String lastName, String email, String password) {
    	XmlUser user = factory.createXmlUser();
    	user.setId(id);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	user.setPassword(password);
    	
        return user;
    }
  
    /**
     * Create an instance of {@link UserFlexOffice }
     * 
     */
    public UserFlexoffice createFlexOfficeUser(Long id, String firstName, String lastName, String email, String password) {
    	UserFlexoffice user = new UserFlexoffice();
    	user.setId(id);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	user.setPassword(password);
    	
        return user;
    }
}
