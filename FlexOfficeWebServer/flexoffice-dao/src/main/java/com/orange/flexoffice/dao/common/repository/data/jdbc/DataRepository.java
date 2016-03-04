package com.orange.flexoffice.dao.common.repository.data.jdbc;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.COUNT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.COUNT_ACTIVE_USERS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.COUNT_ROOM_BY_TYPE_TEMPLATE;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ALL_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_REQUESTED_ROOM_DAILY_AND_TYPE_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_TEACHIN_STATUS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ALL_ROOM_DAILY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ALL_REGIONS_SUMMARY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_REQUESTED_ROOM_DAILY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_LATEST_RESERVED_ROOM_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_KEY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_IDENTIFIER_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_MAC_ADDRESS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_MAIL_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_MAIL_PASSWORD_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ACCESS_TOKEN_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ROOM_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ROOM_ID_OCCUPIED_INFO_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_USER_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_GATEWAY_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_SENSOR_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_NAME_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ROOMSTAT_BY_ROOMID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ROOMSTAT_BY_ROOMINFO_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ONE_TEMPLATE;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_ALL_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_BY_MAC_ADDRESS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_BY_GATEWAY_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_BY_SENSOR_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_BY_IDENTIFIER_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_BY_DAY_ROOM_DAILY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_BY_DATE_ROOM_STATS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_STAT_BY_ROOM_ID_TEMPLATE;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_RESERVED_ROOMSTAT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_ROOMSTAT_BY_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_OCCUPIED_ROOMSTAT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_UNOCCUPIED_ROOMSTAT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_USER_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_USER_ACCESS_TOKEN_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_USER_BY_MAIL_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_TEACHIN_STATUS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_TEACHIN_DATE_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_GATEWAY_STATUS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_GATEWAY_COMMAND_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_ROOM_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_COUNTRY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_GATEWAY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_ALERT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_ROOM_STATUS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_SENSOR_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_SENSOR_STATUS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_SENSOR_ROOM_ID_TEMPLATE;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_USER_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_USER_FROM_USERUI_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_GATEWAY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_ALERT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_ROOM_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_COUNTRY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_ROOMDAILY_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_RESERVED_ROOMSTAT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_OCCUPIED_ROOMSTAT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_SENSOR_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_TEACHIN_SENSOR_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_TEACHIN_STATUS_TEMPLATE;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.orange.flexoffice.dao.common.model.data.Data;
import com.orange.flexoffice.dao.common.repository.data.DataOperations;

public abstract class DataRepository<T extends Data> 
	implements DataOperations<T> {
	
	// FIND QUERIES ------------------
	private   final String findOneQuery;
	protected final String findAllQuery;
	protected final String findAllRoomDailyQuery;
	protected final String findAllRegionsSummaryQuery;
	protected final String findAllDailyQuery;
	protected final String findRequestedDailyAndTypesQuery;
	protected final String findRequestedDailyQuery;
	protected final String findLatestReservedRoomsQuery;
	protected final String findByColumnIdQuery;
	protected final String findByTeachinStatusQuery;
	protected final String findByKeyQuery;
	protected final String findRoomStatByRoomIdQuery;
	protected final String findRoomStatByRoomInfoQuery;
	protected final String findByIdentifierQuery;
	protected final String findByMacAddressQuery;
	protected final String findByColumnMailQuery;
	protected final String findByUserIdQuery;
	protected final String findByColumnMailAndPasswordQuery;
	protected final String findByColumnAccessTokenQuery;
	protected final String findByColumnRoomIdQuery;
	protected final String findByColumnRoomIdAndOccupancyInfoQuery;
	protected final String findByColumnGatewayIdQuery;
	protected final String findByColumnSensorIdQuery;
	protected final String findByColumnNameQuery;
	
	// SAVE QUERIES ---------------------
	protected final String saveUserQuery;
	protected final String saveUserFromUserUIQuery;	
	protected final String saveGatewayQuery;
	protected final String saveRoomQuery;
	protected final String saveCountryQuery;
	protected final String saveTeachinSensorQuery;
	protected final String saveTeachinStatusQuery;
	protected final String saveRoomDailyQuery;
	protected final String saveReservedRoomStatQuery;
	protected final String saveAlertQuery;
	protected final String saveSensorQuery;
	protected final String saveOccupiedRoomStatQuery;
	// UPDATE QUERIES --------------------------------
	protected final String updateReservedRoomStatQuery;
	protected final String updateRoomStatByIdQuery;
	protected final String updateOccupiedRoomStatQuery;
	protected final String updateUnOccupiedRoomStatQuery;
	protected final String updateUserQuery;
	protected final String updateAccessTokenQuery;
	protected final String updateUserByMailQuery;
	protected final String updateGatewayStatusQuery;
	protected final String updateGatewayCommandQuery;
	protected final String updateGatewayQuery;
	protected final String updateRoomQuery;
	protected final String updateCountryQuery;
	protected final String updateTeachinStatusQuery;
	protected final String updateTeachinDateQuery;
	protected final String updateAlertQuery;
	protected final String updateRoomStatusQuery;
	protected final String updateSensorQuery;
	protected final String updateSensorStatusQuery;
	protected final String updateSensorRoomIdQuery;
	// DELETE QUERIES -----------------------------
	protected final String deleteByMacAddressQuery;
	protected final String deleteByGatewayIdQuery;
	protected final String deleteBySensorIdQuery;
	protected final String deleteByIdentifier;
	protected final String deleteByDayQuery;
	protected final String deleteByBeginOccupancyDateQuery;
	protected final String deleteByRoomId;
	private   final String deleteQuery;
	protected   final String deleteAllQuery;
	// COUNT QUERIES --------------
	private   final String countQuery;
	protected final String countActiveUsersQuery;
	protected final String countNbRoomsByTypeQuery;
		
	protected NamedParameterJdbcTemplate jdbcTemplate;
	protected JdbcTemplate jdbcTemplateForTest;
	
	private Class<T> entityClass;
	
	public DataRepository(Class<T> entitClass) {
		this.entityClass = entitClass;
		
		// FIND QUERIES ------------------------------------------------------------------------------
		findOneQuery = String.format(FIND_ONE_TEMPLATE, getTableName());
		findAllQuery = String.format(FIND_ALL_TEMPLATE, getTableName(), getColName());
		findAllRoomDailyQuery = String.format(FIND_ALL_ROOM_DAILY_TEMPLATE, getTableName());
		findAllDailyQuery = String.format(FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE, getTableName());
		findAllRegionsSummaryQuery = String.format(FIND_ALL_REGIONS_SUMMARY_TEMPLATE);
		findRequestedDailyAndTypesQuery = String.format(FIND_REQUESTED_ROOM_DAILY_AND_TYPE_TEMPLATE);
		findRequestedDailyQuery = String.format(FIND_REQUESTED_ROOM_DAILY_TEMPLATE, getTableName());
		findLatestReservedRoomsQuery = String.format(FIND_LATEST_RESERVED_ROOM_TEMPLATE, getTableName());
		findByColumnIdQuery = String.format(FIND_BY_COL_ID_TEMPLATE, getTableName(), getColumnColName());
		findByTeachinStatusQuery = String.format(FIND_BY_TEACHIN_STATUS_TEMPLATE, getTableName());
		findByKeyQuery = String.format(FIND_BY_COL_KEY_TEMPLATE, getTableName());
		findRoomStatByRoomIdQuery = String.format(FIND_ROOMSTAT_BY_ROOMID_TEMPLATE, getTableName());
		findRoomStatByRoomInfoQuery = String.format(FIND_ROOMSTAT_BY_ROOMINFO_TEMPLATE, getTableName());
		findByIdentifierQuery = String.format(FIND_BY_IDENTIFIER_TEMPLATE, getTableName());
		findByMacAddressQuery = String.format(FIND_BY_MAC_ADDRESS_TEMPLATE, getTableName());
		findByColumnMailQuery = String.format(FIND_BY_COL_MAIL_TEMPLATE, getTableName());
		findByColumnMailAndPasswordQuery = String.format(FIND_BY_COL_MAIL_PASSWORD_TEMPLATE, getTableName());
		findByColumnAccessTokenQuery = String.format(FIND_BY_COL_ACCESS_TOKEN_TEMPLATE, getTableName());
		findByColumnRoomIdQuery = String.format(FIND_BY_COL_ROOM_ID_TEMPLATE, getTableName());
		findByColumnRoomIdAndOccupancyInfoQuery = String.format(FIND_BY_COL_ROOM_ID_OCCUPIED_INFO_TEMPLATE, getTableName());
		findByUserIdQuery = String.format(FIND_BY_COL_USER_ID_TEMPLATE, getTableName());
		findByColumnGatewayIdQuery = String.format(FIND_BY_COL_GATEWAY_ID_TEMPLATE, getTableName());
		findByColumnSensorIdQuery = String.format(FIND_BY_COL_SENSOR_ID_TEMPLATE, getTableName());
		findByColumnNameQuery = String.format(FIND_BY_COL_NAME_TEMPLATE, getTableName());
				
		// SAVE QUERIES -------------------------------------------------------
		saveUserQuery = String.format(CREATE_USER_TEMPLATE, getTableName());
		saveUserFromUserUIQuery = String.format(CREATE_USER_FROM_USERUI_TEMPLATE, getTableName());
		saveGatewayQuery = String.format(CREATE_GATEWAY_TEMPLATE, getTableName());
		saveRoomQuery = String.format(CREATE_ROOM_TEMPLATE, getTableName());
		saveCountryQuery = String.format(CREATE_COUNTRY_TEMPLATE, getTableName());
		saveTeachinSensorQuery = String.format(CREATE_TEACHIN_SENSOR_TEMPLATE, getTableName());
		saveTeachinStatusQuery = String.format(CREATE_TEACHIN_STATUS_TEMPLATE, getTableName());
		saveRoomDailyQuery = String.format(CREATE_ROOMDAILY_TEMPLATE, getTableName());
		saveReservedRoomStatQuery = String.format(CREATE_RESERVED_ROOMSTAT_TEMPLATE, getTableName());
		saveOccupiedRoomStatQuery = String.format(CREATE_OCCUPIED_ROOMSTAT_TEMPLATE, getTableName());
		saveAlertQuery = String.format(CREATE_ALERT_TEMPLATE, getTableName());
		saveSensorQuery = String.format(CREATE_SENSOR_TEMPLATE, getTableName());
		
		// UPDATE QUERIES ------------------------------------------------------------------------------
		updateReservedRoomStatQuery = String.format(UPDATE_RESERVED_ROOMSTAT_TEMPLATE, getTableName());
		updateRoomStatByIdQuery = String.format(UPDATE_ROOMSTAT_BY_ID_TEMPLATE, getTableName());
		updateOccupiedRoomStatQuery = String.format(UPDATE_OCCUPIED_ROOMSTAT_TEMPLATE, getTableName());
		updateUnOccupiedRoomStatQuery = String.format(UPDATE_UNOCCUPIED_ROOMSTAT_TEMPLATE, getTableName());
		updateUserQuery = String.format(UPDATE_USER_TEMPLATE, getTableName(), getColumnColName());
		updateAccessTokenQuery = String.format(UPDATE_USER_ACCESS_TOKEN_TEMPLATE, getTableName());
		updateUserByMailQuery = String.format(UPDATE_USER_BY_MAIL_TEMPLATE, getTableName());
		updateGatewayStatusQuery = String.format(UPDATE_GATEWAY_STATUS_TEMPLATE, getTableName());
		updateGatewayCommandQuery = String.format(UPDATE_GATEWAY_COMMAND_TEMPLATE, getTableName());
		updateRoomQuery = String.format(UPDATE_ROOM_TEMPLATE, getTableName(), getColumnColName());
		updateCountryQuery = String.format(UPDATE_COUNTRY_TEMPLATE, getTableName(), getColumnColName());
		updateTeachinStatusQuery = String.format(UPDATE_TEACHIN_STATUS_TEMPLATE, getTableName());
		updateTeachinDateQuery = String.format(UPDATE_TEACHIN_DATE_TEMPLATE, getTableName());
		updateAlertQuery = String.format(UPDATE_ALERT_TEMPLATE, getTableName());
		updateRoomStatusQuery = String.format(UPDATE_ROOM_STATUS_TEMPLATE, getTableName());// set also humidity, temperature & user_id if filled
		updateSensorQuery = String.format(UPDATE_SENSOR_TEMPLATE, getTableName(), getColumnColName());
		updateSensorStatusQuery = String.format(UPDATE_SENSOR_STATUS_TEMPLATE, getTableName(), getColumnColName());
		updateSensorRoomIdQuery = String.format(UPDATE_SENSOR_ROOM_ID_TEMPLATE, getTableName());
		updateGatewayQuery = String.format(UPDATE_GATEWAY_TEMPLATE, getTableName());
		
		// DELETE QUERIES ----------------------------------------------------------------------
		deleteQuery = String.format(REMOVE_TEMPLATE, getTableName());
		deleteAllQuery = String.format(REMOVE_ALL_TEMPLATE, getTableName());
		deleteByMacAddressQuery = String.format(REMOVE_BY_MAC_ADDRESS_TEMPLATE, getTableName());
		deleteByGatewayIdQuery = String.format(REMOVE_BY_GATEWAY_ID_TEMPLATE, getTableName());
		deleteBySensorIdQuery = String.format(REMOVE_BY_SENSOR_ID_TEMPLATE, getTableName());
		deleteByIdentifier = String.format(REMOVE_BY_IDENTIFIER_TEMPLATE, getTableName());
		deleteByDayQuery = String.format(REMOVE_BY_DAY_ROOM_DAILY_TEMPLATE, getTableName());
		deleteByBeginOccupancyDateQuery = String.format(REMOVE_BY_DATE_ROOM_STATS_TEMPLATE, getTableName());
		deleteByRoomId = String.format(REMOVE_STAT_BY_ROOM_ID_TEMPLATE, getTableName());
		
		// COUNT QUERIES ----------------------------------------------------------------------
		countQuery = String.format(COUNT_TEMPLATE, getTableName());
		countActiveUsersQuery = String.format(COUNT_ACTIVE_USERS_TEMPLATE, getTableName());
		countNbRoomsByTypeQuery = String.format(COUNT_ROOM_BY_TYPE_TEMPLATE, getTableName());
		
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		// For Tests
		jdbcTemplateForTest = new JdbcTemplate(dataSource);
	}
	
	@Override
	public T findOne(Long id) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		
		T data = null;
			data = jdbcTemplate.queryForObject(
					findOneQuery, 
					paramMap, 
					new BeanPropertyRowMapper<T>(entityClass)
				);
		return data;
	}
	
	@Override
	public void delete(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		jdbcTemplate.update(deleteQuery, paramMap);
	}
	
	@Override
	public Long count() {
		return jdbcTemplate.getJdbcOperations().queryForObject(countQuery, Long.class);
	}
	
	
	@Override
	public List<Data> findByColumnId(String columnId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", columnId);
		List<T> datas = jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<T>(entityClass)
			);
		return new ArrayList<Data>(datas);
	}
	
	protected abstract String getTableName();
	protected abstract String getColumnColName();
	protected abstract String getColName();

}
