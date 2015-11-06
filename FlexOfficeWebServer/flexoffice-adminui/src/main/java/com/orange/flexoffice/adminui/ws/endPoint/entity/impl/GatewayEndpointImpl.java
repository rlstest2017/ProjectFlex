package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.model.EDeviceStatus;
import com.orange.flexoffice.adminui.ws.model.ErrorModel;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
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
	public GatewayOutput2 getGateway(String gatewayId) {
		
		try {
			GatewayDto data = gatewayManager.find(Long.valueOf(gatewayId));
			
			GatewayOutput2 gateway = factory.createGatewayOutput2();
			gateway.setId(data.getMacAdress());
			gateway.setName(data.getName());
			gateway.setDesc(data.getDescription());
			gateway.setStatus(EDeviceStatus.valueOf(data.getStatus().toString()));
			gateway.setLastPollingDate(data.getLastPollingDate().getTime());
			
			List<RoomDao> rooms = data.getRooms();
			
			for (RoomDao roomDao : rooms) {
				RoomOutput rm = new RoomOutput();
				rm.setId(roomDao.getColumnId());
				rm.setName(roomDao.getName());
				gateway.getRooms().add(rm);
			}
		
			return factory.createGatewayOutput2(gateway).getValue();
			
			} catch (DataNotExistsException e){
				ErrorModel errorModel =  factory.createErrorModel();
				errorModel.setCode(EnumErrorModel.ERROR_25.code());
				errorModel.setMessage(EnumErrorModel.ERROR_25.value());
				
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
		
		
	}
	
	
	
	
	
}
