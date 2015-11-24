package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import java.util.Date;

import javax.naming.AuthenticationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.userui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.userui.ws.model.Token;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
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
	private SystemManager systemManager;
	
	@Autowired
	private TestManager testManager;

	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public UserSummary getUserCurrent(String auth, String origin) {
		LOGGER.info( "Begin call UserUi.UserEndpoint.getUserCurrent at: " + new Date() );

		try {
			// TODO Change to get current user from DB
			UserDao data = userManager.find(1);

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
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_8, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in UserUi.UserEndpoint.getUserCurrent with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	
	@Override
	public Response login(String auth, String origin) {
		try {
			UserDao userToken = systemManager.processLogin(auth, false);
			Token token = factory.createToken();
			token.setAccessToken(userToken.getAccessToken());
			token.setExpiredDate(userToken.getExpiredTokenDate().getTime());
			token.setIsCreatedFromUserUi(userToken.getIsCreatedFromUserui());
					
			if (origin != null) {
				LOGGER.debug("Origin value is :" + origin);
				return Response.ok(token).status(200)
			            .header("Access-Control-Allow-Origin", "*")
			            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
			            .header("Access-Control-Allow-Credentials", "true")
			            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
			            .header("Access-Control-Max-Age", "1209600")
			            .build();
			} else {
	        	LOGGER.debug("Origin value is null");
	        	return Response.status(200).entity(token).build();
	        }
		} catch (DataNotExistsException e) {
				LOGGER.debug("DataNotExistsException in login() SystemEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.METHOD_NOT_ALLOWED));
		} catch (AuthenticationException e) {
				LOGGER.debug("AuthenticationException in login() SystemEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_33, Response.Status.UNAUTHORIZED));
		}catch (RuntimeException ex) {
				LOGGER.debug("RuntimeException in login() SystemEndpointImpl with message :" + ex.getMessage(), ex);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}



	@Override
	public Response logout(String token, String origin) {
		
		systemManager.processLogout(token);
		
		if (origin != null) {
			LOGGER.debug("Origin value is :" + origin);
			return Response.ok().status(200)
		            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		            .header("Access-Control-Max-Age", "1209600")
		            .build();
		} else {
        	LOGGER.debug("Origin value is null");
        	return Response.status(200).build();
        }
	}

	@Override
	public Response options() {
	    return Response.ok("")
	            .header("Access-Control-Allow-Origin", "*")
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-auth-token")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
	            .header("Access-Control-Max-Age", "1209600")
	            .build();
	}

	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

}
