package com.orange.flexoffice.business.common.service.data.impl;

//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.repository.data.jdbc.InitTestRepository;

/**
 * Manages {@link GatewayDto}.
 * 
 * @author oab
 */
@Service("TestManager")
@Transactional
public class TestManagerImpl implements TestManager {
	
	//private final Logger LOGGER = Logger.getLogger(TestManagerImpl.class);
	
	@Autowired
	private InitTestRepository testRepository;
	
	
	@Override
	public boolean executeInitTestFile() {
		return testRepository.executeInitTestData();
	}
	
}