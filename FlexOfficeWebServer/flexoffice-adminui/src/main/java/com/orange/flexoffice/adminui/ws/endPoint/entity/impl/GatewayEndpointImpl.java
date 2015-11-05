package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.model.ErrorModel;
import com.orange.flexoffice.adminui.ws.model.Gateway;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;


public class GatewayEndpointImpl implements GatewayEndpoint {
	
	private final Logger LOGGER = Logger.getLogger(GatewayEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	//private UserFlexofficeManager userManager;
	
	@Override
	public Gateway getGateway(String gatewayId) {
//		UserFlexoffice data = userManager.find(Long.valueOf(userId));
//		
//		if (data == null) {
//			ErrorModel errorModel = new ErrorModel();
//			errorModel.setCode(EnumErrorModel.ERROR_8.code());
//			errorModel.setMessage(EnumErrorModel.ERROR_8.value());
//			
//			ResponseBuilderImpl builder = new ResponseBuilderImpl();
//			builder.status(Response.Status.NOT_FOUND);
//			builder.entity(errorModel);
//			Response response = builder.build();
//			throw new WebApplicationException(response);
//		}
//		
//		
//		User user = factory.createUser();
//		user.setId(data.getColumnId());
//		user.setEmail(data.getEmail());
//		user.setFirstName(data.getFirstName());
//		user.setLastName(data.getLastName());
	
		//return factory.createUser(user).getValue();
		return null;
	}
	
	
	
	
	
}
