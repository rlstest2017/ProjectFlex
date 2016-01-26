package com.orange.flexoffice.dao.common.repository.data.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.repository.data.ConfigurationDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.ConfigurationDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage configuration table

LAST_CONNECTION_DURATION => is used for get activeUsersCount()
OCCUPANCY_TIMEOUT => the interval of time in which PUT /sensor/{sensorId} is called. Than the occupancy_info of rooms
are sent !!!

BOOKING_DURATION => the interval of time after what the RESERVED state is set to TIMEOUT by the 
checkReservationTimeOut() planified task.

TEACHIN_TIMEOUT => the interval of time after what the Teachin state is set to ENDED by the 
checkTeachinTimeOutMethod() planified task.

KEEP_STAT_DATA_IN_DAYS => the interval of time before what the room_stats & room_occupancy_daily are deleted 
by purgeStatsDataTestMethod() planified task.

---------------------------------------------------------------------------------------*/

@Repository
public class ConfigurationDaoRepository extends DataRepository<ConfigurationDao> implements ConfigurationDaoOperations {

	
	public ConfigurationDaoRepository() {
		super(ConfigurationDao.class);
	}

	@Override
	public ConfigurationDao findByKey(String key) {
		SqlParameterSource paramMap = new MapSqlParameterSource("key", key);
		ConfigurationDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByKeyQuery, 
				paramMap, 
				new BeanPropertyRowMapper<ConfigurationDao>(ConfigurationDao.class)
			);
		return data;
	}

	@Override
	protected String getTableName() {
		return ConfigurationDaoMetadata.CONFIGURATION_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return null;
	}

	@Override
	protected String getColName() {
		return null;
	}

}
