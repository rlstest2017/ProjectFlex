package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.userui.service.data.UserFlexofficeManager;
import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;
import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.userui.ws.model.ErrorModel;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;
import com.orange.flexoffice.userui.ws.model.XmlUser;



public class UserEndpointImpl implements UserEndpoint {
	
	private final Logger LOGGER = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private UserFlexofficeManager userManager;
	
	/**
	 * Get a specific user.
	 * 
	 * @see UserEndpoint#getUser(String)
	 */
	public XmlUser getUser(String userId) {
	UserFlexoffice data = userManager.find(Long.valueOf(userId));
		
		if (data == null) {
			ErrorModel errorModel = new ErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_4.code());
			errorModel.setMessage(EnumErrorModel.ERROR_4.value());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.NOT_FOUND);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		
		
		XmlUser user = factory.createXmlUser();
		user.setEmail(data.getEmail());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());
		user.setPassword(data.getPassword());
		
		return factory.createUser(user).getValue();

	}

	@Override
	public XmlUser addUser(XmlUser xmlUser) throws DataAlreadyExistsException {
		
        LOGGER.info( "Begin call doPost method for UserEndpoint at: " + new Date() );
        
		UserFlexoffice user = new UserFlexoffice();
		user.setEmail(xmlUser.getEmail());
		user.setFirstName(xmlUser.getFirstName());
		user.setLastName(xmlUser.getLastName());
		user.setPassword(xmlUser.getPassword());
		
		 if (LOGGER.isDebugEnabled()) {
	            LOGGER.debug( "Begin call addUser(XmlUser xmlUser) method for UserEndpoint, with parameters :");
	            final StringBuffer message = new StringBuffer( 100 );
	            message.append( "email :" );
	            message.append( xmlUser.getEmail() );
	            message.append( "\n" );
	            message.append( "firstname :" );
	            message.append( xmlUser.getFirstName() );
	            message.append( "\n" );
	            message.append( "lastname :" );
	            message.append( xmlUser.getLastName() );
	            message.append( "\n" );
	            message.append( "password :" );
	            message.append( xmlUser.getPassword() );
	            message.append( "\n" );
	            LOGGER.debug( message.toString() );
	    }
		
		
		user = userManager.save(user);
		
		XmlUser returnedUser = factory.createXmlUser();
		returnedUser.setId(user.getColumnId());
		
		LOGGER.info( "End call doPost method for UserEndpoint at: " + new Date() );
		
		return factory.createUser(returnedUser).getValue();
	}

	@Override
	public Response updateUser(String id, XmlUser xmlUser) {
		LOGGER.info( "Begin call doPut method for UserEndpoint at: " + new Date() );
		
		UserFlexoffice user = new UserFlexoffice();
		user.setId(Long.valueOf(id));
		user.setEmail(xmlUser.getEmail());
		user.setFirstName(xmlUser.getFirstName());
		user.setLastName(xmlUser.getLastName());
		user.setPassword(xmlUser.getPassword());
		
		try {
		user = userManager.update(user);
	
		} catch (DataNotExistsException e){
			ErrorModel errorModel = new ErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_6.code());
			errorModel.setMessage(EnumErrorModel.ERROR_6.value());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.NOT_FOUND);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		
		URI location = uriInfo.getAbsolutePathBuilder()
				.path(UserEndpoint.class, "getUser")
				.build(user.getId());
		LOGGER.info( "End call doPut method for UserEndpoint at: " + new Date() );
		return Response.created(location).build();
	}

	@Override
	public Response removeUser(String id) {
		try {
			userManager.delete(Long.valueOf(id));
		} catch (NumberFormatException | DataNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.noContent().build();
		//return Response.status(Status.NOT_FOUND).build();
	}
	
}
