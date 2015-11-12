package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.EGatewayStatus;
import com.orange.flexoffice.gatewayapi.ws.model.ESensorStatus;
import com.orange.flexoffice.gatewayapi.ws.model.GatewaySummary;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.Room;
import com.orange.flexoffice.gatewayapi.ws.model.SensorSummary;
import com.orange.flexoffice.gatewayapi.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.object.RoomDto;


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
			gateway.setStatus(EGatewayStatus.valueOf(gatewayDao.getStatus().toString()));
			gatewayList.add(gateway);
		}
		
		return gatewayList;
	}

	@Override
	public List<Room> getGateway(String gatewayId) {
		try {
			List<Room> rooms = new ArrayList<Room>();
			
			List<RoomDto> data = gatewayManager.findGatewayRooms(Long.valueOf(gatewayId));
			
			LOGGER.debug("There is: " + data.size() + " rooms");
			
			for (RoomDto roomDto : data) {
				Room room = factory.createRoom();
				room.setId(roomDto.getId());
				room.setName(roomDto.getName());
				room.setOccupancyTimeout(BigInteger.valueOf(5)); // TODO from config file	
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
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
			}
	}

	@Override
	public boolean executeGatewaysTestFile() {
		return gatewayManager.executeGatewaysTestFile();
	}
	
}
