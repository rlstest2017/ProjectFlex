package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;


import java.util.ArrayList;
import java.util.List;

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
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;


public class GatewayEndpointImpl implements GatewayEndpoint {
	
	private final Logger LOGGER = Logger.getLogger(GatewayEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private GatewayManager gatewayManager;
	
	@Override
	public Gateway getGateway(String gatewayId) {
		GatewayDto data = gatewayManager.find(Long.valueOf(gatewayId));
		
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
		
		
		Gateway gateway = factory.createGateway();
		gateway.setId(data.getId());
		gateway.setName(data.getName());
		gateway.setDesc(data.getDescription());
		// TODO gateway.setLastPollingDate(data.getLastPollingDate());
		
		List<RoomDao> rooms = data.getRooms();
		
		for (RoomDao roomDao : rooms) {
			RoomSummary rm = new RoomSummary();
			rm.setId(roomDao.getColumnId());
			rm.setName(roomDao.getName());
			gateway.getRooms().add(rm);
		}
		
		return factory.createGateway(gateway).getValue();

	}
	
	
	
	
	
}
