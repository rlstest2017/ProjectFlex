package com.orange.flexoffice.gatewayapi.ws.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.orange.flexoffice.gatewayapi.ws.model.ErrorModel;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;

/**
 * ErrorMessageHandler
 * @author oab
 *
 */
public class ErrorMessageHandler {
    
	private final ObjectFactory factory = new ObjectFactory();
	
	/** Create error message for exception
	 * 
	 * @param error
	 * @param status
	 * @return message
	 */
	public Response createErrorMessage(final EnumErrorModel error, Status status) {
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
