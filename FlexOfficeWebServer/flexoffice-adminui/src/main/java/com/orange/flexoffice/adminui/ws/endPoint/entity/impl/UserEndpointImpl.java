package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.model.ErrorModel;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.adminui.ws.model.UserSummary;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.UserManager;


/**
 * @author oab
 *
 */
public class UserEndpointImpl implements UserEndpoint {

	private final Logger LOGGER = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Context
	private UriInfo uriInfo;
	@Autowired
	private UserManager userManager;

	@Override
	public List<UserSummary> getUsers() {
		List<UserDao> dataList = userManager.findAllUsers();

		if (dataList == null) {

			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_4, Response.Status.NOT_FOUND));
		
		}

		LOGGER.debug("List of users : " + dataList.size());

		List<UserSummary> userList = new ArrayList<UserSummary>();

		for (UserDao UserDao : dataList) {
			UserSummary user = factory.createUserSummary();
			user.setId(UserDao.getColumnId());
			user.setEmail(UserDao.getEmail());
			user.setFirstName(UserDao.getFirstName());
			user.setLastName(UserDao.getLastName());
			user.setLabel(UserDao.getFirstName() + " " + UserDao.getLastName());

			userList.add(user);
		}

		return userList;
	}

	@Override
	public User getUser(String userId) {
		UserDao data = userManager.find(Long.valueOf(userId));

		if (data == null) {

			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_8, Response.Status.NOT_FOUND));
		
		}


		User user = factory.createUser();
		user.setId(data.getColumnId());
		user.setEmail(data.getEmail());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());

		return factory.createUser(user).getValue();
	}

	@Override
	public User addUser(UserInput userInput) {

		LOGGER.info( "Begin call doPost method for UserEndpoint at: " + new Date() );

		UserDao user = new UserDao();
		user.setEmail(userInput.getEmail());
		user.setFirstName(userInput.getFirstName());
		user.setLastName(userInput.getLastName());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addUser(XmlUser xmlUser) method for UserEndpoint, with parameters :");
			final StringBuffer message = new StringBuffer( 1000 );
			message.append( "email :" );
			message.append( userInput.getEmail() );
			message.append( "\n" );
			message.append( "firstname :" );
			message.append( userInput.getFirstName() );
			message.append( "\n" );
			message.append( "lastname :" );
			message.append( userInput.getLastName() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			user = userManager.save(user);

		} catch (DataAlreadyExistsException e) {
			
			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_9, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		User returnedUser = factory.createUser();
		returnedUser.setId(user.getColumnId());

		LOGGER.info( "End call doPost method for UserEndpoint at: " + new Date() );

		return factory.createUser(returnedUser).getValue();
	}

	@Override
	public Response updateUser(String id, UserInput userInput) {
		LOGGER.info( "Begin call doPut method for UserEndpoint at: " + new Date() );

		UserDao user = new UserDao();
		user.setId(Long.valueOf(id));
		user.setEmail(userInput.getEmail());
		user.setFirstName(userInput.getFirstName());
		user.setLastName(userInput.getLastName());

		try {
			user = userManager.update(user);

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_6, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call doPut method for UserEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response removeUser(String id) {

		try {

			userManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_7, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		return Response.noContent().build();
	}

	@Override
	public UserDao findByUserMail(String userEmail) {

		return userManager.findByUserMail(userEmail);
	}


	
	
	
	/** Create error message for exception
	 * 
	 * @param error
	 * @param status
	 * @return message
	 */
	private Response createErrorMessage(final EnumErrorModel error, Status status) {
		ErrorModel errorModel = factory.createErrorModel();
		errorModel.setCode(error.code());
		errorModel.setMessage(error.value());

		ResponseBuilderImpl builder = new ResponseBuilderImpl();
		builder.status(status);
		builder.entity(errorModel);
		Response response = builder.build();

		return response;
	}

}
