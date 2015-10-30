package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.userui.service.data.UserFlexofficeManager;
import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;
import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;
import com.orange.flexoffice.userui.ws.model.XmlUser;


public class UserEndpointImpl implements UserEndpoint {
	
	private final Logger logger = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private UserFlexofficeManager userManager;
	
	/**
	 * Get a specific user.
	 * 
	 * @see UserEndpoint#getUser(String)
	 */
	public JAXBElement<XmlUser> getUser(String userId) {
	UserFlexoffice data = userManager.find(Long.valueOf(userId));
		
		if (data == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		
		XmlUser user = factory.createXmlUser();
		user.setEmail(data.getEmail());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());
		user.setPassword(data.getPassword());
		
		return factory.createUser(user);

	}

	@Override
	public Response addUser(XmlUser xmlUser) throws DataAlreadyExistsException {
		UserFlexoffice user = new UserFlexoffice();
		user.setEmail(xmlUser.getEmail());
		user.setFirstName(xmlUser.getFirstName());
		user.setLastName(xmlUser.getLastName());
		user.setPassword(xmlUser.getPassword());
				
		user = userManager.save(user);
		
		return Response.noContent().build();
	}

	@Override
	public Response updateUser(String id, XmlUser xmlUser) {
		UserFlexoffice user = new UserFlexoffice();
		user.setId(Long.valueOf(id));
		user.setEmail(xmlUser.getEmail());
		user.setFirstName(xmlUser.getFirstName());
		user.setLastName(xmlUser.getLastName());
		user.setPassword(xmlUser.getPassword());
				
		user = userManager.update(user);
		
		return Response.noContent().build();
	}

	@Override
	public Response removeUser(String id) {
		userManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
	
}
