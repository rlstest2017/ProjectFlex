package com.orange.flexoffice.business.common.service.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.repository.data.jdbc.InitTestRepository;

/**
 * Manages {@link GatewayDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("TestManager")
@Transactional
public class TestManagerImpl implements TestManager {
	
	@Autowired
	private InitTestRepository testRepository;

	@Override
	public boolean executeInitTestFile() {
		return testRepository.executeInitTestData();
	}

	@Override
	public boolean executeDropTables() {
		return testRepository.executeDropTables();
	}

	@Override
	public boolean initRoomStatsTableForUserUI() {
		return testRepository.initRoomStatsTableForUserUi();
	}

	@Override
	public boolean initRoomStatsTableForAdminUI() {
		return testRepository.initRoomStatsTableForAdminUi();
	}

	@Override
	public boolean initRoomDailyOccupancyTable() {
		return testRepository.initRoomDailyOccupancyTable();
	}

	@Override
	public boolean initRoomMonthlyOccupancyTable() {
		return testRepository.initRoomMonthlyOccupancyTable();
	}

	@Override
	public boolean initTeachinSensorsTable() {
		return testRepository.initTeachinSensorsTable();
	}

	@Override
	public boolean setTeachinSensorsTable() {
		return testRepository.setTeachinSensorsTable();
	}
	
}
