package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.business.gatewayapi.dto.GatewayCommand;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;

/**
 * Manages {@link GatewayDto}.
 * 
 * @author oab
 */
@Service("GatewayManager")
@Transactional
public class GatewayManagerImpl implements GatewayManager {
	
	private final Logger LOGGER = Logger.getLogger(GatewayManagerImpl.class);
	
	@Autowired
	private GatewayDaoRepository gatewayRepository;
	
	@Autowired
	private RoomDaoRepository roomRepository;
	
	@Autowired
	private SensorDaoRepository sensorRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<GatewayDao> findAllGateways() {
		return gatewayRepository.findAllGateways();
	}

	@Override
	@Transactional(readOnly=true)
	public List<RoomDto> findGatewayRooms(long gatewayId) {
		
		List<RoomDto> roomDtoList = new ArrayList<RoomDto>();
		
		List<RoomDao> roomsDao = roomRepository.findByGatewayId(gatewayId);
		
		LOGGER.debug("There is: " + roomsDao.size() + " rooms for gateway :" + gatewayId );
		
		for (RoomDao roomDao : roomsDao) {
			RoomDto roomDto = new RoomDto();
			roomDto.setId(roomDao.getColumnId());
			roomDto.setName(roomDao.getName());
			List<SensorDao> sonsensDao = getSensors(roomDao.getId());
			
			LOGGER.debug("There is: " + sonsensDao.size() + " sensors for room :" + roomDao.getColumnId());
			
			if ((sonsensDao != null)&&(!sonsensDao.isEmpty())) {
				roomDto.setSensors(sonsensDao);					
			}
			
			roomDtoList.add(roomDto);
		}
		
		return roomDtoList;
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public GatewayDto find(long gatewayId)  throws DataNotExistsException {
		GatewayDao gatewayDao = gatewayRepository.findOne(gatewayId);
		
		if (gatewayDao == null) {
			LOGGER.debug("gateway by id " + gatewayId + " is not found");
			throw new DataNotExistsException("Gateway not exist");
		}
		
		GatewayDto dto = new GatewayDto();
		dto.setId(String.valueOf(gatewayId));
		dto.setDescription(gatewayDao.getDescription());
		dto.setLastPollingDate(gatewayDao.getLastPollingDate());
		dto.setMacAddress(gatewayDao.getMacAddress());
		dto.setName(gatewayDao.getName());
		dto.setStatus(E_GatewayStatus.valueOf(gatewayDao.getStatus()));
		
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug( "Return find(long gatewayId) method for GatewayManagerImpl, with parameters :");
            final StringBuffer message = new StringBuffer( 100 );
            message.append( "id :" );
            message.append( String.valueOf(gatewayId) );
            message.append( "\n" );
            message.append( "macAddress :" );
            message.append( gatewayDao.getMacAddress() );
            message.append( "\n" );
            message.append( "name :" );
            message.append( gatewayDao.getName() );
            message.append( "\n" );
            LOGGER.debug( message.toString() );
        }
		
		List<RoomDao> roomsList = getRooms(gatewayId);
		if ((roomsList != null) && (roomsList.size() > 0)) { 
			dto.setRooms(roomsList);
			dto.setActivated(true);			
		} else {
			dto.setActivated(false);
		}
		
		return dto;
	}

	@Override
	public GatewayDao save(GatewayDao gatewayDao) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayDao update(GatewayDao gatewayDao) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayCommand updateStatus(GatewayDao gatewayDao) throws DataNotExistsException {
		Long gatewayId = gatewayDao.getId();
		GatewayDao gatewayFound = gatewayRepository.findOne(gatewayId);
		
		if (gatewayFound == null) {
			LOGGER.debug("gateway by id " + gatewayId + " is not found");
			throw new DataNotExistsException("GatewayDao already saves.");
		}
		
		// Saves UserDao
		//return gatewayRepository.updateSatus(GatewayDao);
		return null;
	}

	@Override
	public void delete(long id) throws DataNotExistsException {
		// TODO Auto-generated method stub
		
	}

	// used for tests
	@Override
	public boolean executeGatewaysTestFile() {
		return gatewayRepository.executeGatewaysTestFile();
	}
	
	/**
	 * getRooms
	 * @param gatewayId
	 * @return
	 */
	private List<RoomDao> getRooms(long gatewayId) {
		List<RoomDao> roomsDao =roomRepository.findByGatewayId(gatewayId);
		
		return roomsDao;
	}
	
	/**
	 * getSensors
	 * @param roomId
	 * @return
	 */
	private List<SensorDao> getSensors(long roomId) {
		List<SensorDao> sensorsDao = sensorRepository.findByRoomId(roomId);
		
		return sensorsDao;
	}
	
}
