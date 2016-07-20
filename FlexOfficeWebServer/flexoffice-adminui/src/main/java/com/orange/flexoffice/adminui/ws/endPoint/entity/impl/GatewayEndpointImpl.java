package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.model.EDeviceStatus;
import com.orange.flexoffice.adminui.ws.model.GatewayInput;
import com.orange.flexoffice.adminui.ws.model.GatewayInput3;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.GatewaySummary;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;


public class GatewayEndpointImpl implements GatewayEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(GatewayEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
		
	@Autowired
	private GatewayManager gatewayManager;
	@Autowired
	private TestManager testManager;
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
			gateway.setMacAddress(gatewayDao.getMacAddress());
			gateway.setName(gatewayDao.getName());
			if ((gatewayDao.getStatus().equals(E_GatewayStatus.ONTEACHIN.toString())) || (gatewayDao.getStatus().equals(E_GatewayStatus.ONLINE.toString()))) {
				gateway.setStatus(EDeviceStatus.ONLINE);
			} else if (gatewayDao.getStatus().equals(E_GatewayStatus.OFFLINE.toString())) {
				gateway.setStatus(EDeviceStatus.OFFLINE);
			} else {
				gateway.setStatus(EDeviceStatus.UNSTABLE);
			}
			if (gatewayDao.getLastPollingDate() != null) {
				gateway.setLastPollingDate(gatewayDao.getLastPollingDate().getTime());
			}
			
			gatewayList.add(gateway);
		}
		
		return gatewayList;
	}

	@Override
	public GatewayOutput2 getGateway(String macAddress) {
		
		try {
			GatewayDto data = gatewayManager.findByMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
			
			GatewayOutput2 gateway = factory.createGatewayOutput2();
			gateway.setMacAddress(data.getMacAddress());
			gateway.setName(data.getName());
			gateway.setDesc(data.getDescription());
			if ((data.getStatus().equals(E_GatewayStatus.ONTEACHIN.toString())) || (data.getStatus().equals(E_GatewayStatus.ONLINE.toString()))) {
				gateway.setStatus(EDeviceStatus.ONLINE);
			} else if (data.getStatus().equals(E_GatewayStatus.OFFLINE.toString())) {
				gateway.setStatus(EDeviceStatus.OFFLINE);
			} else {
				gateway.setStatus(EDeviceStatus.UNSTABLE);
			}
			
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
				LOGGER.debug("DataNotExistsException in getGateway() GatewayEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_25, Response.Status.NOT_FOUND));
			} catch (RuntimeException ex){
				LOGGER.debug("RuntimeException in getGateway() GatewayEndpointImpl with message :" + ex.getMessage(), ex);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
			}
		
		
	}

	@Override
	public GatewayOutput addGateway(GatewayInput3 gateway) {
		LOGGER.debug( "Begin call doPost method for GatewayEndpoint at: " + new Date() );

		GatewayDao gatewayDao = new GatewayDao();
		gatewayDao.setMacAddress(gateway.getMacAddress().toLowerCase().replaceAll("-", ":"));
		gatewayDao.setName(gateway.getName().trim());
		gatewayDao.setDescription(gateway.getDesc());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addGateway(GatewayInput1 gateway) method of GatewayEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "\n" );
			message.append( "macAddress :" );
			message.append( gateway.getMacAddress() );
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
			LOGGER.debug("DataAlreadyExistsException in addGateway() GatewayEndpointImpl with message :" + e.getMessage(), e);			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_22, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in addGateway() GatewayEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		GatewayOutput returnedGateway = factory.createGatewayOutput();
		returnedGateway.setMacAddress(gatewayDao.getMacAddress());

		LOGGER.debug( "End call doPost method for GatewayEndpoint at: " + new Date() );

		return factory.createGatewayOutput(returnedGateway).getValue();

	}

	@Override
	public Response updateGateway(String macAddress, GatewayInput gateway) {
		LOGGER.debug( "Begin call doPut method for GatewayEndpoint at: " + new Date() );

		GatewayDao gatewayDao = new GatewayDao();
		gatewayDao.setMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
		gatewayDao.setName(gateway.getName().trim());
		gatewayDao.setDescription(gateway.getDesc());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateGateway(String id, GatewayInput gateway) method of GatewayEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "macAddress :" );
			message.append( macAddress );
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
			gatewayDao = gatewayManager.update(gatewayDao);

		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in updateGateway() GatewayEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_23, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in updateGateway() GatewayEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		GatewayOutput returnedGateway = factory.createGatewayOutput();
		returnedGateway.setMacAddress(gatewayDao.getMacAddress());

		LOGGER.debug( "End call doPut method for GatewayEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response removeGateway(String macAddress) {
		try {

			gatewayManager.delete(macAddress.toLowerCase().replaceAll("-", ":"));

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in removeGateway() GatewayEndpointImpl with message :" + e.getMessage(), e);			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_24, Response.Status.NOT_FOUND));
			
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in GatewayEndpoint.removeGateway with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_24, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in removeGateway() GatewayEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		return Response.noContent().build();
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	@Override
	public GatewayDto findByMacAddress(String macAddress) throws DataNotExistsException {
		return gatewayManager.findByMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
	}

	@Override
	public boolean executeDropTables() {
		return testManager.executeDropTables();
	}

		
	
}
