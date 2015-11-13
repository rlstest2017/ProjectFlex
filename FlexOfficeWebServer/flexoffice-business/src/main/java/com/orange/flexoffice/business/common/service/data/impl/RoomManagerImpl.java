package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;
import com.orange.flexoffice.dao.common.model.object.RoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link RoomDto}.
 * 
 * @author oab
 */
@Service("RoomManager")
@Transactional
public class RoomManagerImpl implements RoomManager {
	
	private final Logger LOGGER = Logger.getLogger(RoomManagerImpl.class);
	
	@Autowired
	private RoomDaoRepository roomRepository;

	@Autowired
	private GatewayDaoRepository gatewayRepository;
		
	@Autowired
	private SensorDaoRepository sensorRepository;
		
	@Autowired
	private UserDaoRepository userRepository;

	@Override
	@Transactional(readOnly=true)
	public List<RoomDao> findAllRooms() {
		return roomRepository.findAllRooms();
	}


	
	@Override
	@Transactional(readOnly=true)
	public RoomDto find(long roomId)  throws DataNotExistsException {
		RoomDao roomDao = roomRepository.findOne(roomId);
		
		if (roomDao == null) {
			LOGGER.debug("room by id " + roomId + " is not found");
			throw new DataNotExistsException("Room not exist");
		}
		
		RoomDto dto = new RoomDto();
		dto.setId(String.valueOf(roomId));
		dto.setDescription(roomDao.getDescription());
		dto.setName(roomDao.getName());
		dto.setAddress(roomDao.getAddress());
		dto.setCapacity(roomDao.getCapacity());
		dto.setStatus(E_RoomStatus.valueOf(roomDao.getStatus()));
		dto.setType(E_RoomType.valueOf(roomDao.getType()));
		
		UserDao userDao = userRepository.findOne(roomDao.getUserId());
		if (userDao != null) {
			dto.setUser(userDao);
		}
		
		List<SensorDao> sensorsDao = sensorRepository.findByRoomId(roomId);
		if ((sensorsDao != null) && (sensorsDao.size() > 0)) {
			dto.setSensors(sensorsDao);
		}
				
		GatewayDao gatewayDao = gatewayRepository.findOne(roomDao.getGatewayId());
		if (gatewayDao != null) {
			dto.setGateway(gatewayDao);
		}
		
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug( "Return find(long roomId) method for RoomManagerImpl, with parameters :");
            final StringBuffer message = new StringBuffer( 1000 );
            message.append( "id :" );
            message.append( String.valueOf(roomId) );
            message.append( "\n" );
            message.append( "name :" );
            message.append( roomDao.getName() );
            message.append( "\n" );
            LOGGER.debug( message.toString() );
        }
				
		return dto;
	}

	@Override
	public RoomDto save(RoomDto roomDto) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomDto update(RoomDto UserFlexoffice) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) throws DataNotExistsException {
		// TODO Auto-generated method stub
		
	}
	
	
}
