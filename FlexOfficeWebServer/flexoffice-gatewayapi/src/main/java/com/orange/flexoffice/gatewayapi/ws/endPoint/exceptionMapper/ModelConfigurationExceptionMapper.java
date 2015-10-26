package com.orange.flexoffice.gatewayapi.ws.endPoint.exceptionMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.Logger;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;

public class ModelConfigurationExceptionMapper implements ExceptionMapper<DataAlreadyExistsException> {
	
	private final Logger logger = Logger.getLogger(ModelConfigurationExceptionMapper.class);

	public Response toResponse(DataAlreadyExistsException exception) {
		logger.error("An error is occured.", exception);
		return Response.serverError().build();
	}

}
