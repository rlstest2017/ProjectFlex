package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;
import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.model.ErrorModel;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserSummary;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.userui.service.data.UserFlexofficeManager;


public class UserEndpointImpl implements UserEndpoint {
	
	private final Logger LOGGER = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private UserFlexofficeManager userManager;
	
	@Override
	public List<UserSummary> getUsers() {
		List<UserFlexoffice> dataList = userManager.findAllUsers();
		
		if (dataList == null) {
			ErrorModel errorModel = new ErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_4.code());
			errorModel.setMessage(EnumErrorModel.ERROR_4.value());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.NOT_FOUND);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		
		LOGGER.debug("List of users : " + dataList.size());
		
		List<UserSummary> userList = new ArrayList<UserSummary>();
		
		for (UserFlexoffice userFlexoffice : dataList) {
			UserSummary user = factory.createUserSummary();
			user.setId(userFlexoffice.getColumnId());
			user.setEmail(userFlexoffice.getEmail());
			user.setFirstName(userFlexoffice.getFirstName());
			user.setLastName(userFlexoffice.getLastName());
			user.setLabel(userFlexoffice.getFirstName() + " " + userFlexoffice.getLastName());
			
			userList.add(user);
		}
		
		return userList;
	}
	
	@Override
	public User getUser(String userId) {
	UserFlexoffice data = userManager.find(Long.valueOf(userId));
		
		if (data == null) {
			ErrorModel errorModel = new ErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_8.code());
			errorModel.setMessage(EnumErrorModel.ERROR_8.value());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.NOT_FOUND);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		
		
		User user = factory.createUser();
		user.setId(data.getColumnId());
		user.setEmail(data.getEmail());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());
	
		return factory.createUser(user).getValue();
	}

	@Override
	public User addUser(User xmlUser) {
		
        LOGGER.info( "Begin call doPost method for UserEndpoint at: " + new Date() );
        
		UserFlexoffice user = new UserFlexoffice();
		user.setEmail(xmlUser.getEmail());
		user.setFirstName(xmlUser.getFirstName());
		user.setLastName(xmlUser.getLastName());
		
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
	            LOGGER.debug( message.toString() );
	    }
		
		 try {
			 user = userManager.save(user);
			
		} catch (DataAlreadyExistsException e) {
			ErrorModel errorModel = new ErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_9.code());
			errorModel.setMessage(EnumErrorModel.ERROR_9.value());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.METHOD_NOT_ALLOWED);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		} catch (RuntimeException ex) {
			ErrorModel errorModel = new ErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_32.code());
			errorModel.setMessage(ex.getMessage());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		
		
		User returnedUser = factory.createUser();
		returnedUser.setId(user.getColumnId());
		
		LOGGER.info( "End call doPost method for UserEndpoint at: " + new Date() );
		
		return factory.createUser(returnedUser).getValue();
	}

	@Override
	public Response updateUser(String id, User xmlUser) {
		LOGGER.info( "Begin call doPut method for UserEndpoint at: " + new Date() );
		
		UserFlexoffice user = new UserFlexoffice();
		user.setId(Long.valueOf(id));
		user.setEmail(xmlUser.getEmail());
		user.setFirstName(xmlUser.getFirstName());
		user.setLastName(xmlUser.getLastName());
		
		try {
		user = userManager.update(user);
	
		} catch (DataNotExistsException e){
			ErrorModel errorModel =  factory.createErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_6.code());
			errorModel.setMessage(EnumErrorModel.ERROR_6.value());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.NOT_FOUND);
			builder.entity(errorModel);
			Response response = builder.build();
			throw new WebApplicationException(response);
		} catch (RuntimeException ex){
			ErrorModel errorModel =  factory.createErrorModel();
			errorModel.setCode(EnumErrorModel.ERROR_32.code());
			errorModel.setMessage(ex.getMessage());
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
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
		
			} catch (DataNotExistsException e){
				ErrorModel errorModel =  factory.createErrorModel();
				errorModel.setCode(EnumErrorModel.ERROR_7.code());
				errorModel.setMessage(EnumErrorModel.ERROR_7.value());
				
				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.NOT_FOUND);
				builder.entity(errorModel);
				Response response = builder.build();
				throw new WebApplicationException(response);
			} catch (RuntimeException ex){
				ErrorModel errorModel =  factory.createErrorModel();
				errorModel.setCode(EnumErrorModel.ERROR_32.code());
				errorModel.setMessage(ex.getMessage());
				
				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.entity(errorModel);
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
		
		
		return Response.noContent().build();
	}

	
	
}
