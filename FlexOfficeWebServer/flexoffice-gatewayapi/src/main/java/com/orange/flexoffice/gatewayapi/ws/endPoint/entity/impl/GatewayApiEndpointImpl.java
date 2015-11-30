package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.GatewayApiEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ECommandModel;
import com.orange.flexoffice.gatewayapi.ws.model.EGatewayStatus;
import com.orange.flexoffice.gatewayapi.ws.model.ESensorStatus;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayInput;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayReturn;
import com.orange.flexoffice.gatewayapi.ws.model.GatewaySummary;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.Room;
import com.orange.flexoffice.gatewayapi.ws.model.SensorSummary;
import com.orange.flexoffice.gatewayapi.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.business.gatewayapi.dto.GatewayCommand;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;


public class GatewayApiEndpointImpl implements GatewayApiEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(GatewayApiEndpointImpl.class);
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
			gateway.setId(gatewayDao.getMacAddress());
			gateway.setStatus(EGatewayStatus.valueOf(gatewayDao.getStatus().toString()));
			gatewayList.add(gateway);
		}
		
		return gatewayList;
	}

	@Override
	public List<Room> getGateway(String gatewayMacAddress) {
		try {
			List<Room> rooms = new ArrayList<Room>();
			
			List<RoomDto> data = gatewayManager.findGatewayRooms(gatewayMacAddress);
			
			LOGGER.debug("There is: " + data.size() + " rooms");
			
			for (RoomDto roomDto : data) {
				Room room = factory.createRoom();
				room.setId(BigInteger.valueOf(roomDto.getId()));
				room.setName(roomDto.getName());
				room.setOccupancyTimeout(BigInteger.valueOf(3)); // TODO from config file	
				List<SensorDao> sensors = roomDto.getSensors();
				for (SensorDao sensorDao : sensors) {
					SensorSummary sr = new SensorSummary();
					sr.setId(sensorDao.getIdentifier());
					sr.setProfile(sensorDao.getProfile());
					sr.setStatus(ESensorStatus.valueOf(sensorDao.getStatus()));
					// add sensor To room
					room.getSensors().add(sr);
				}
				// add room To List
				rooms.add(room);
			}
			
			return rooms;
			
			}  catch (RuntimeException ex){
				LOGGER.debug("RuntimeException in getGateway() GatewayEndpointImpl with message :" + ex.getMessage(), ex);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
			}
	}

	@Override
	public GatewayReturn updateGateway(String macAddress, GatewayInput gateway) {
		try {
		LOGGER.info( "Begin call doPut method for GatewayEndpoint at: " + new Date() );
		GatewayDao gatewayDao = new GatewayDao();
		gatewayDao.setMacAddress(macAddress);
		gatewayDao.setStatus(gateway.getGatewayStatus().toString());
		
			GatewayCommand command = gatewayManager.updateStatus(gatewayDao);
			
			GatewayReturn returnCommand = factory.createGatewayReturn();
			if (command.getRoomId() > 1) {
				returnCommand.setRoomId(BigInteger.valueOf(command.getRoomId()));
			}
			returnCommand.setCommand(ECommandModel.valueOf(command.getCommand().toString()));
			
			LOGGER.info( "End call doPut method for GatewayEndpoint at: " + new Date() );
			return returnCommand;
			
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in updateGateway() GatewayEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_23, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in updateGateway() GatewayEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	@Override
	public GatewayDto findByMacAddress(String macAddress) throws DataNotExistsException {
		return gatewayManager.findByMacAddress(macAddress);
	}
	
}
