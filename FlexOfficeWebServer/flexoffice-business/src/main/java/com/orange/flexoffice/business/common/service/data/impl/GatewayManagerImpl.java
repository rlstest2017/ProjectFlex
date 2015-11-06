package com.orange.flexoffice.business.common.service.data.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;

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
	
	
	@Override
	@Transactional(readOnly=true)
	public List<GatewayDto> findAllGateways() {
		
		List<GatewayDao> daoList = gatewayRepository.findAllGateways();
		// TODO
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public GatewayDto find(long gatewayId) {
		GatewayDao gatewayDao = gatewayRepository.findOne(gatewayId);
		
		GatewayDto dto = new GatewayDto();
		dto.setId(String.valueOf(gatewayId));
		dto.setActivated(gatewayDao.isActivated());
		dto.setDescription(gatewayDao.getDescription());
		dto.setLastPollingDate(gatewayDao.getLastPollingDate());
		dto.setMacAdress(gatewayDao.getMacAdress());
		dto.setName(gatewayDao.getName());
		dto.setStatus(E_GatewayStatus.valueOf(gatewayDao.getStatus()));
		dto.setRooms(getRooms(gatewayDao.getRoomsId()));
		
		return dto;
	}

	@Override
	public GatewayDto save(GatewayDto gatewayDto) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayDto update(GatewayDto UserFlexoffice) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) throws DataNotExistsException {
		// TODO Auto-generated method stub
		
	}

	private List<RoomDao> getRooms(org.postgresql.jdbc4.Jdbc4Array roomsId) {
		
		List<RoomDao> rooms = new ArrayList<RoomDao>();
		
		Integer[] rmsId;
		try {
			rmsId = (Integer[])roomsId.getArray();
			List<Integer> rmsIdList = Arrays.asList(rmsId);
			for (Integer roomId : rmsIdList) {
				List<RoomDao> roomDao =roomRepository.findByRoomId(Long.valueOf(roomId.toString()));
				rooms. add(roomDao.get(0));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rooms;
	}
}
