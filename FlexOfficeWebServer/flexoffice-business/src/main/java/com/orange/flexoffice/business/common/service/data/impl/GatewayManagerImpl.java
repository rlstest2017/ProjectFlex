package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;

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
		// TODO
		return null;
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

}
