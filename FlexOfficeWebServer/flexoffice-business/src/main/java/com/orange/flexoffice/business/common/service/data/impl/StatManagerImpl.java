package com.orange.flexoffice.business.common.service.data.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.StatManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.object.SimpleStatDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;

/**
 * StatManagerImpl
 * @author oab
 */
@Service("StatManager")
@Transactional
public class StatManagerImpl implements StatManager {
	
	private static final Logger LOGGER = Logger.getLogger(StatManagerImpl.class);
	
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private RoomDailyOccupancyDaoRepository roomDailyRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	
	@Autowired
	private DateTools dateTools;

	@Override
	@Transactional(readOnly=true)
	public List<SimpleStatDto> getPopularStats() {
		// 1 - Get Date with DATE_BEGIN_DAY & DATE_END_DAY parameters
		ConfigurationDao beginDay = configRepository.findByKey(E_ConfigurationKey.DATE_BEGIN_DAY.toString());
		String  beginDayValue = beginDay.getValue(); // in hh:mm
		ConfigurationDao endDay = configRepository.findByKey(E_ConfigurationKey.DATE_END_DAY.toString());
		String  endDayValue = endDay.getValue(); // in hh:mm
		
		// 2 - Process the Dates
		Date beginDayDate = dateTools.dateBeginDay(beginDayValue);
		Date endDayDate = dateTools.dateEndDay(endDayValue); 
		
		// 3 - Calculate duration between beginDayDate & endDayDate in seconds
		Long duration = dateTools.calculateDuration(beginDayDate, endDayDate);
		
		
		return null;
	}
	
	
	
}
