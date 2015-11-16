package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.adminui.ws.model.UserSummary;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
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
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<UserSummary> getUsers() {

		LOGGER.info( "Begin call getUsers method for UserEndpoint at: " + new Date() );

		List<UserDao> dataList = userManager.findAllUsers();

		if (dataList == null) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_4, Response.Status.NOT_FOUND));
		
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

		LOGGER.info( "End call getUsers method for UserEndpoint at: " + new Date() );

		return userList;
	}

	@Override
	public User getUser(String userId) {

		LOGGER.info( "Begin call getUser method for UserEndpoint at: " + new Date() );

		try {
			UserDao data = userManager.find(Long.valueOf(userId));

			User user = factory.createUser();
			user.setId(data.getColumnId());
			user.setEmail(data.getEmail());
			user.setFirstName(data.getFirstName());
			user.setLastName(data.getLastName());

			LOGGER.info( "End call getUser method for UserEndpoint at: " + new Date() );

			return factory.createUser(user).getValue();

		} catch (DataNotExistsException e){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_8, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}
	}

	@Override
	public User addUser(UserInput userInput) {

		LOGGER.info( "Begin call addUser method for UserEndpoint at: " + new Date() );

		UserDao user = new UserDao();
		user.setEmail(userInput.getEmail());
		user.setFirstName(userInput.getFirstName());
		user.setLastName(userInput.getLastName());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addUser(UserInput userInput) method of UserEndpoint, with parameters :");
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
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_9, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		User returnedUser = factory.createUser();
		returnedUser.setId(user.getColumnId());

		LOGGER.info( "End call addUser method for UserEndpoint at: " + new Date() );

		return factory.createUser(returnedUser).getValue();
	}

	@Override
	public Response updateUser(String id, UserInput userInput) {
		
		LOGGER.info( "Begin call updateUser method for UserEndpoint at: " + new Date() );

		UserDao user = new UserDao();
		user.setId(Long.valueOf(id));
		user.setEmail(userInput.getEmail());
		user.setFirstName(userInput.getFirstName());
		user.setLastName(userInput.getLastName());

		try {
			user = userManager.update(user);

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_6, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call updateUser method for UserEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response removeUser(String id) {

		LOGGER.info( "Begin call removeUser method for UserEndpoint at: " + new Date() );

		try {

			userManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_7, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call removeUser method for UserEndpoint at: " + new Date() );

		return Response.noContent().build();
	}

	@Override
	public UserDao findByUserMail(String userEmail) throws DataNotExistsException {

		return userManager.findByUserMail(userEmail);
	}

}
