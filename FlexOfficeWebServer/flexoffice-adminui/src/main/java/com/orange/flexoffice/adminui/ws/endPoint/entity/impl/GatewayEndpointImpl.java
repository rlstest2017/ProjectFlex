package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.model.EDeviceStatus;
import com.orange.flexoffice.adminui.ws.model.GatewayInput;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.GatewaySummary;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;


public class GatewayEndpointImpl implements GatewayEndpoint {
	
	private final Logger LOGGER = Logger.getLogger(GatewayEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Context
	private UriInfo uriInfo;
	@Autowired
	private GatewayManager gatewayManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public List<GatewaySummary> getGateways() {
			
			List<GatewayDao> dataList = gatewayManager.findAllGateways();
		
		if (dataList == null) {
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_21, Response.Status.NOT_FOUND));
		}
		
		LOGGER.debug("List of gateways : " + dataList.size());
		
		List<GatewaySummary> gatewayList = new ArrayList<GatewaySummary>();
		
		for (GatewayDao gatewayDao : dataList) {
			GatewaySummary gateway = factory.createGatewaySummary();
			gateway.setId(gatewayDao.getColumnId());
			gateway.setName(gatewayDao.getName());
			if (gatewayDao.getStatus().equals(E_GatewayStatus.ONTEACHIN.toString())) {
				gateway.setStatus(EDeviceStatus.ONLINE);
			} else {
			gateway.setStatus(EDeviceStatus.valueOf(gatewayDao.getStatus().toString()));
			}
			if (gatewayDao.getLastPollingDate() != null) {
				gateway.setLastPollingDate(gatewayDao.getLastPollingDate().getTime());
			}
			
			gatewayList.add(gateway);
		}
		
		return gatewayList;
	}

	@Override
	public GatewayOutput2 getGateway(String gatewayId) {
		
		try {
			GatewayDto data = gatewayManager.find(Long.valueOf(gatewayId));
			
			GatewayOutput2 gateway = factory.createGatewayOutput2();
			gateway.setId(data.getMacAddress());
			gateway.setName(data.getName());
			gateway.setDesc(data.getDescription());
			gateway.setStatus(EDeviceStatus.valueOf(data.getStatus().toString()));
			if (data.getLastPollingDate() != null) {
				gateway.setLastPollingDate(data.getLastPollingDate().getTime());
			}
			
			List<RoomDao> rooms = data.getRooms();
			
			for (RoomDao roomDao : rooms) {
				RoomOutput rm = new RoomOutput();
				rm.setId(roomDao.getColumnId());
				rm.setName(roomDao.getName());
				gateway.getRooms().add(rm);
			}
		
			return factory.createGatewayOutput2(gateway).getValue();
			
			} catch (DataNotExistsException e){
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_25, Response.Status.NOT_FOUND));
			} catch (RuntimeException ex){
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
			}
		
		
	}

	@Override
	public GatewayOutput addGateway(GatewayInput gateway) {
		LOGGER.info( "Begin call doPost method for GatewayEndpoint at: " + new Date() );

		GatewayDao gatewayDao = new GatewayDao();
		gatewayDao.setName(gateway.getName());
		gatewayDao.setDescription(gateway.getDesc());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addGateway(GatewayInput1 gateway) method of GatewayEndpoint, with parameters :");
			final StringBuffer message = new StringBuffer( 1000 );
			message.append( "\n" );
			message.append( "name :" );
			message.append( gateway.getName() );
			message.append( "\n" );
			message.append( "desc :" );
			message.append( gateway.getDesc() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			gatewayDao = gatewayManager.save(gatewayDao);

		} catch (DataAlreadyExistsException e) {
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_22, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		GatewayOutput returnedGateway = factory.createGatewayOutput();
		returnedGateway.setId(gatewayDao.getColumnId());

		LOGGER.info( "End call doPost method for GatewayEndpoint at: " + new Date() );

		return factory.createGatewayOutput(returnedGateway).getValue();

	}

	@Override
	public Response updateGateway(String id, GatewayInput gateway) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response removeGateway(String id) {
		try {

			gatewayManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_24, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		return Response.noContent().build();
	}

	@Override
	public boolean executeGatewaysTestFile() {
		return gatewayManager.executeGatewaysTestFile();
	}

		
	
}
