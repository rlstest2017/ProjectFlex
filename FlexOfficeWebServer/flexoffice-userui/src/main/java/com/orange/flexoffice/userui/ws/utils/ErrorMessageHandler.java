package com.orange.flexoffice.userui.ws.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.orange.flexoffice.userui.ws.model.ErrorModel;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;
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

		return Response.ok(errorModel).status(status)
		        .header("Access-Control-Allow-Origin", "*")
		        .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		        .header("Access-Control-Allow-Credentials", "true")
		        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		        .header("Access-Control-Max-Age", "1209600")
		        .build();
	}
	
}
