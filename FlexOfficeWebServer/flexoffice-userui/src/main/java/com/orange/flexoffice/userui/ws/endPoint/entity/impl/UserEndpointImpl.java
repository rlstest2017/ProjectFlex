package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;
import com.orange.flexoffice.userui.ws.model.XmlUser;



public class UserEndpointImpl implements UserEndpoint {
	
	private final Logger LOGGER = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private UserManager userManager;
	@Override
	public XmlUser getUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public XmlUser addUser(XmlUser user) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response updateUser(String id, XmlUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response removeUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
		
}
