package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.userui.ws.model.ErrorModel;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;
import com.orange.flexoffice.userui.ws.model.UserSummary;



public class UserEndpointImpl implements UserEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(UserEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	
	@Autowired
	private UserManager userManager;

	@Autowired
	private TestManager testManager;

	
	@Override
	public UserSummary getUserCurrent() {
		LOGGER.info( "Begin call UserUi.UserEndpoint.getUserCurrent at: " + new Date() );

		try {
			// TODO Change to get current user from DB
			UserDao data = userManager.find(1);
			//
			

			UserSummary userSummary = factory.createUserSummary();
			userSummary.setId(data.getId().toString());
			userSummary.setLabel(data.getFirstName() + " " + data.getLastName());
			userSummary.setFirstName(data.getFirstName());
			userSummary.setLastName(data.getLastName());
			userSummary.setEmail(data.getEmail());

			LOGGER.info( "End call UserEndpoint.getUser at: " + new Date() );

			return factory.createUserSummary(userSummary).getValue();

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in UserUi.UserEndpoint.getUserCurrent with message :", e);
			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_8, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in UserUi.UserEndpoint.getUserCurrent with message :", ex);
			throw new WebApplicationException(createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	

	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}


	
	
	/**
	 * @param error
	 * @param status
	 * @return
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
