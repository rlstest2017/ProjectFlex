package com.orange.flexoffice.dao.common.repository.data.jdbc;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.*;

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
	protected final String findCountriesHaveRoomsQuery;
	protected final String findAllRoomDailyQuery;
	protected final String findAllMeetingRoomGroupsConfigurationQuery;
	protected final String findAllMeetingRoomDailyQuery;
	protected final String findAllRegionsSummaryQuery;
	protected final String findAllCitiesSummaryQuery;
	protected final String findAllBuildingsSummaryQuery;
	protected final String findAllDailyQuery;
	protected final String findAllDailyQueryMeetingRoom;
	protected final String findRequestedDailyAndTypesQuery;
	protected final String findRequestedDailyAndTypesQueryMeetingRoom;
	protected final String findRequestedDailyQuery;
	protected final String findRequestedDailyQueryMeetingRoom;
	protected final String findLatestReservedRoomsQuery;
	protected final String findByColumnIdQuery;
	protected final String findByRegionIdQuery;
	protected final String findByCityIdQuery;
	protected final String findByBuildingIdQuery;
	protected final String findPreferenceByUserIdQuery;
	protected final String findByTeachinStatusQuery;
	protected final String findByKeyQuery;
	protected final String findRoomStatByRoomIdQuery;
	protected final String findMeetingRoomStatByMeetingRoomIdQuery;
	protected final String findRoomStatByRoomInfoQuery;
	protected final String findMeetingRoomStatByMeetingRoomInfoQuery;
	protected final String findByIdentifierQuery;
	protected final String findByMacAddressQuery;
	protected final String findByMeetingRoomIdQuery;
	protected final String findByColumnMailQuery;
	protected final String findByUserIdQuery;
	protected final String findBySensorIdentifierQuery;
	protected final String findByColumnMailAndPasswordQuery;
	protected final String findByColumnAccessTokenQuery;
	protected final String findByColumnRoomIdQuery;
	protected final String findByColumnRoomIdAndOccupancyInfoQuery;
	protected final String findByColumnGatewayIdQuery;
	protected final String findByColumnAgentIdQuery;
	protected final String findByColumnDashboardIdQuery;
	protected final String findRoomsByCountryIdQuery;
	protected final String findRoomsByRegionIdQuery;
	protected final String findRoomsByCityIdQuery;
	protected final String findRoomsByBuildingIdQuery;
	protected final String findRoomsByBuildingIdAndFloorQuery;
	protected final String findMeetingRoomsByCountryIdQuery;
	protected final String findMeetingRoomsByRegionIdQuery;
	protected final String findMeetingRoomsByCityIdQuery;
	protected final String findMeetingRoomsByBuildingIdQuery;
	protected final String findMeetingRoomsByBuildingIdAndFloorQuery;
	protected final String findByColumnCountryIdQuery;
	protected final String findRegionsHaveRoomsByCountryIdQuery;
	protected final String findCitiesHaveRoomsByRegionIdQuery;
	protected final String findBuildingsHaveRoomsByCityIdQuery;
	protected final String findByColumnRegionIdQuery;
	protected final String findByColumnCityIdQuery;
	protected final String findByColumnBuildingIdQuery;
	protected final String findByColumnBuildingIdAndFloorQuery;
	protected final String findByColumnSensorIdQuery;
	protected final String findByColumnNameQuery;
	
	// SAVE QUERIES ---------------------
	protected final String saveUserQuery;
	protected final String saveUserFromUserUIQuery;	
	protected final String saveGatewayQuery;
	protected final String saveMeetingRoomGroupsConfigurationQuery;
	protected final String saveAgentQuery;
	protected final String saveDashboardQuery;
	protected final String saveRoomQuery;
	protected final String saveMeetingRoomQuery;
	protected final String saveCountryQuery;
	protected final String saveRegionQuery;
	protected final String saveCityQuery;
	protected final String saveBuildingQuery;
	protected final String savePreferenceQuery;
	protected final String saveTeachinSensorQuery;
	protected final String saveTeachinStatusQuery;
	protected final String saveRoomDailyQuery;
	protected final String saveMeetingRoomDailyQuery;
	protected final String saveReservedRoomStatQuery;
	protected final String saveAlertQuery;
	protected final String saveSensorQuery;
	protected final String saveOccupiedRoomStatQuery;
	protected final String saveOccupiedMeetingRoomStatQuery;
	// UPDATE QUERIES --------------------------------
	protected final String updateReservedRoomStatQuery;
	protected final String updateRoomStatByIdQuery;
	protected final String updateMeetingRoomStatByIdQuery;
	protected final String updateUnOccupiedRoomStatQuery;
	protected final String updateUnOccupiedMeetingRoomStatQuery;
	protected final String updateOccupiedRoomStatQuery;
	protected final String updateUserQuery;
	protected final String updateAccessTokenQuery;
	protected final String updateUserByMailQuery;
	protected final String updateGatewayStatusQuery;
	protected final String updateAgentStatusQuery;
	protected final String updateAgentMeetingRoomIdQuery;
	protected final String updateDashboardStatusQuery;
	protected final String updateGatewayCommandQuery;
	protected final String updateGatewayQuery;
	protected final String updateMeetingRoomGroupsConfigurationQuery;
	protected final String updateAgentQuery;
	protected final String updateDashboardQuery;
	protected final String updateRoomQuery;
	protected final String updateMeetingRoomQuery;
	protected final String updateCountryQuery;
	protected final String updateRegionQuery;
	protected final String updateCityQuery;
	protected final String updateBuildingQuery;
	protected final String updatePreferenceQuery;
	protected final String updateTeachinStatusQuery;
	protected final String updateTeachinDateQuery;
	protected final String updateAlertQuery;
	protected final String updateRoomStatusQuery;
	protected final String updateMeetingRoomStatusQuery;
	protected final String updateSensorQuery;
	protected final String updateSensorStatusQuery;
	protected final String updateSensorRoomIdQuery;
	// DELETE QUERIES -----------------------------
	protected final String deleteByMacAddressQuery;
	protected final String deleteByBuildingIdQuery;
	protected final String deleteByBuildingIdAndFloorQuery;
	protected final String deleteByGatewayIdQuery;
	protected final String deleteBySensorIdQuery;
	protected final String deleteByIdentifier;
	protected final String deleteByDayQuery;
	protected final String deleteByDayQueryMeetingRoom;
	protected final String deleteByBeginOccupancyDateQuery;
	protected final String deleteByBeginOccupancyDateQueryMeetingRoom;
	protected final String deleteByRoomId;
	protected final String deleteByMeetingRoomId;
	protected final String deletePrefByCountryIdQuery;
	protected final String deletePrefByRegionIdQuery;
	protected final String deletePrefByCityIdQuery;
	protected final String deletePrefByBuildingIdQuery;
	protected final String deletePrefByUserIdQuery;
	private   final String deleteQuery;
	protected final String deleteAllQuery;
	// COUNT QUERIES --------------
	private   final String countQuery;
	protected final String countSensorsByTypeAndRoomIdQuery;
	protected final String countActiveUsersQuery;
	protected final String countNbRoomsByTypeQuery;
	protected final String countNbMeetingRoomsByTypeQuery;
		
	protected NamedParameterJdbcTemplate jdbcTemplate;
	protected JdbcTemplate jdbcTemplateForTest;
	
	private Class<T> entityClass;
	
	public DataRepository(Class<T> entitClass) {
		this.entityClass = entitClass;
		
		// FIND QUERIES ------------------------------------------------------------------------------
		findOneQuery = String.format(FIND_ONE_TEMPLATE, getTableName());
		findAllQuery = String.format(FIND_ALL_TEMPLATE, getTableName(), getColName());
		findCountriesHaveRoomsQuery = String.format(FIND_COUNTRIES_HAVE_ROOMS_TEMPLATE, getTableName(), getColName());
		findAllMeetingRoomDailyQuery = String.format(FIND_ALL_MEETINGROOM_DAILY_TEMPLATE, getTableName());
		findAllRoomDailyQuery = String.format(FIND_ALL_ROOM_DAILY_TEMPLATE, getTableName());
		findAllMeetingRoomGroupsConfigurationQuery = String.format(FIND_ALL_MEETINGROOM_GROUPS_CONFIGURATION_TEMPLATE, getTableName());
		findAllDailyQuery = String.format(FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE, getTableName());
		findAllDailyQueryMeetingRoom = String.format(FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE_MEETINGROOM, getTableName());
		findAllRegionsSummaryQuery = String.format(FIND_ALL_REGIONS_SUMMARY_TEMPLATE);
		findAllCitiesSummaryQuery = String.format(FIND_ALL_CITIES_SUMMARY_TEMPLATE);
		findAllBuildingsSummaryQuery = String.format(FIND_ALL_BUILDINGS_SUMMARY_TEMPLATE);
		findRequestedDailyAndTypesQuery = String.format(FIND_REQUESTED_ROOM_DAILY_AND_TYPE_TEMPLATE);
		findRequestedDailyAndTypesQueryMeetingRoom = String.format(FIND_REQUESTED_MEETINGROOM_DAILY_AND_TYPE_TEMPLATE);
		findRequestedDailyQuery = String.format(FIND_REQUESTED_ROOM_DAILY_TEMPLATE, getTableName());
		findRequestedDailyQueryMeetingRoom = String.format(FIND_REQUESTED_MEETINGROOM_DAILY_TEMPLATE, getTableName());
		findLatestReservedRoomsQuery = String.format(FIND_LATEST_RESERVED_ROOM_TEMPLATE, getTableName());
		findByColumnIdQuery = String.format(FIND_BY_COL_ID_TEMPLATE, getTableName(), getColumnColName());
		findByRegionIdQuery = String.format(FIND_REGION_DTO_BY_COL_ID_TEMPLATE, getTableName());
		findByCityIdQuery = String.format(FIND_CITY_DTO_BY_COL_ID_TEMPLATE, getTableName());
		findByBuildingIdQuery = String.format(FIND_BUILDING_DTO_BY_COL_ID_TEMPLATE, getTableName());
		findPreferenceByUserIdQuery = String.format(FIND_PREFERENCE_BY_USER_ID_TEMPLATE, getTableName(), getColName());
		findByTeachinStatusQuery = String.format(FIND_BY_TEACHIN_STATUS_TEMPLATE, getTableName());
		findByKeyQuery = String.format(FIND_BY_COL_KEY_TEMPLATE, getTableName());
		findRoomStatByRoomIdQuery = String.format(FIND_ROOMSTAT_BY_ROOMID_TEMPLATE, getTableName());
		findMeetingRoomStatByMeetingRoomIdQuery = String.format(FIND_MEETINGROOMSTAT_BY_MEETINGROOMID_TEMPLATE, getTableName());
		findRoomStatByRoomInfoQuery = String.format(FIND_ROOMSTAT_BY_ROOMINFO_TEMPLATE, getTableName());
		findMeetingRoomStatByMeetingRoomInfoQuery = String.format(FIND_MEETINGROOMSTAT_BY_MEETINGROOMINFO_TEMPLATE, getTableName());
		findByIdentifierQuery = String.format(FIND_BY_IDENTIFIER_TEMPLATE, getTableName());
		findByMacAddressQuery = String.format(FIND_BY_MAC_ADDRESS_TEMPLATE, getTableName());
		findByMeetingRoomIdQuery = String.format(FIND_BY_MEETINGROOM_ID_TEMPLATE, getTableName());
		findByColumnMailQuery = String.format(FIND_BY_COL_MAIL_TEMPLATE, getTableName());
		findByColumnMailAndPasswordQuery = String.format(FIND_BY_COL_MAIL_PASSWORD_TEMPLATE, getTableName());
		findByColumnAccessTokenQuery = String.format(FIND_BY_COL_ACCESS_TOKEN_TEMPLATE, getTableName());
		findByColumnRoomIdQuery = String.format(FIND_BY_COL_ROOM_ID_TEMPLATE, getTableName());
		findByColumnRoomIdAndOccupancyInfoQuery = String.format(FIND_BY_COL_ROOM_ID_OCCUPIED_INFO_TEMPLATE, getTableName());
		findByUserIdQuery = String.format(FIND_BY_COL_USER_ID_TEMPLATE, getTableName());
		findBySensorIdentifierQuery = String.format(FIND_BY_SENSOR_IDENTIFIER_TEMPLATE, getTableName());
		findByColumnGatewayIdQuery = String.format(FIND_BY_COL_GATEWAY_ID_TEMPLATE, getTableName());
		findByColumnAgentIdQuery = String.format(FIND_BY_COL_AGENT_ID_TEMPLATE, getTableName());
		findByColumnDashboardIdQuery = String.format(FIND_BY_COL_DASHBOARD_ID_TEMPLATE, getTableName());
		findRoomsByCountryIdQuery = String.format(FIND_ROOMS_BY_COUNTRY_ID_TEMPLATE, getTableName());
		findRoomsByRegionIdQuery = String.format(FIND_ROOMS_BY_REGION_ID_TEMPLATE, getTableName());
		findRoomsByCityIdQuery = String.format(FIND_ROOMS_BY_CITY_ID_TEMPLATE, getTableName());
		findRoomsByBuildingIdQuery = String.format(FIND_ROOMS_BY_BUILDING_ID_TEMPLATE, getTableName());
		findRoomsByBuildingIdAndFloorQuery = String.format(FIND_ROOMS_BY_BUILDING_ID_AND_FLOOR_TEMPLATE, getTableName());
		findMeetingRoomsByCountryIdQuery = String.format(FIND_MEETINGROOMS_BY_COUNTRY_ID_TEMPLATE, getTableName());
		findMeetingRoomsByRegionIdQuery = String.format(FIND_MEETINGROOMS_BY_REGION_ID_TEMPLATE, getTableName());
		findMeetingRoomsByCityIdQuery = String.format(FIND_MEETINGROOMS_BY_CITY_ID_TEMPLATE, getTableName());
		findMeetingRoomsByBuildingIdQuery = String.format(FIND_MEETINGROOMS_BY_BUILDING_ID_TEMPLATE, getTableName());
		findMeetingRoomsByBuildingIdAndFloorQuery = String.format(FIND_MEETINGROOMS_BY_BUILDING_ID_AND_FLOOR_TEMPLATE, getTableName());
		findByColumnCountryIdQuery = String.format(FIND_BY_COL_COUNTRY_ID_TEMPLATE, getTableName());
		findRegionsHaveRoomsByCountryIdQuery = String.format(FIND_REGIONS_HAVE_ROOMS_BY_COUNTRY_ID_TEMPLATE, getTableName());
		findCitiesHaveRoomsByRegionIdQuery = String.format(FIND_CITIES_HAVE_ROOMS_BY_REGION_ID_TEMPLATE, getTableName());
		findBuildingsHaveRoomsByCityIdQuery = String.format(FIND_BUILDINGS_HAVE_ROOMS_BY_CITY_ID_TEMPLATE, getTableName());
		findByColumnRegionIdQuery = String.format(FIND_BY_COL_REGION_ID_TEMPLATE, getTableName());
		findByColumnCityIdQuery = String.format(FIND_BY_COL_CITY_ID_TEMPLATE, getTableName());
		findByColumnBuildingIdQuery = String.format(FIND_BY_COL_BUILDING_ID_TEMPLATE, getTableName());
		findByColumnBuildingIdAndFloorQuery = String.format(FIND_BY_COL_BUILDING_ID_AND_FLOOR_TEMPLATE, getTableName());
		findByColumnSensorIdQuery = String.format(FIND_BY_COL_SENSOR_ID_TEMPLATE, getTableName());
		findByColumnNameQuery = String.format(FIND_BY_COL_NAME_TEMPLATE, getTableName());
				
		// SAVE QUERIES -------------------------------------------------------
		saveUserQuery = String.format(CREATE_USER_TEMPLATE, getTableName());
		saveUserFromUserUIQuery = String.format(CREATE_USER_FROM_USERUI_TEMPLATE, getTableName());
		saveGatewayQuery = String.format(CREATE_GATEWAY_TEMPLATE, getTableName());
		saveMeetingRoomGroupsConfigurationQuery = String.format(CREATE_MEETINGROOM_GROUPS_CONFIGURATION_TEMPLATE, getTableName());
		saveAgentQuery = String.format(CREATE_AGENT_TEMPLATE, getTableName());
		saveDashboardQuery = String.format(CREATE_DASHBOARD_TEMPLATE, getTableName());
		saveRoomQuery = String.format(CREATE_ROOM_TEMPLATE, getTableName());
		saveMeetingRoomQuery = String.format(CREATE_MEETINGROOM_TEMPLATE, getTableName());
		saveCountryQuery = String.format(CREATE_COUNTRY_TEMPLATE, getTableName());
		saveRegionQuery = String.format(CREATE_REGION_TEMPLATE, getTableName());
		saveCityQuery = String.format(CREATE_CITY_TEMPLATE, getTableName());
		saveBuildingQuery = String.format(CREATE_BUILDING_TEMPLATE, getTableName());
		savePreferenceQuery = String.format(CREATE_PREFERENCE_TEMPLATE, getTableName());
		saveTeachinSensorQuery = String.format(CREATE_TEACHIN_SENSOR_TEMPLATE, getTableName());
		saveTeachinStatusQuery = String.format(CREATE_TEACHIN_STATUS_TEMPLATE, getTableName());
		saveRoomDailyQuery = String.format(CREATE_ROOMDAILY_TEMPLATE, getTableName());
		saveMeetingRoomDailyQuery = String.format(CREATE_MEETINGROOM_DAILY_TEMPLATE, getTableName());
		saveReservedRoomStatQuery = String.format(CREATE_RESERVED_ROOMSTAT_TEMPLATE, getTableName());
		saveOccupiedRoomStatQuery = String.format(CREATE_OCCUPIED_ROOMSTAT_TEMPLATE, getTableName());
		saveOccupiedMeetingRoomStatQuery = String.format(CREATE_OCCUPIED_MEETINGROOMSTAT_TEMPLATE, getTableName());
		saveAlertQuery = String.format(CREATE_ALERT_TEMPLATE, getTableName());
		saveSensorQuery = String.format(CREATE_SENSOR_TEMPLATE, getTableName());
		
		// UPDATE QUERIES ------------------------------------------------------------------------------
		updateReservedRoomStatQuery = String.format(UPDATE_RESERVED_ROOMSTAT_TEMPLATE, getTableName());
		updateRoomStatByIdQuery = String.format(UPDATE_ROOMSTAT_BY_ID_TEMPLATE, getTableName());
		updateMeetingRoomStatByIdQuery = String.format(UPDATE_MEETINGROOMSTAT_BY_ID_TEMPLATE, getTableName());
		updateOccupiedRoomStatQuery = String.format(UPDATE_OCCUPIED_ROOMSTAT_TEMPLATE, getTableName());
		updateUnOccupiedRoomStatQuery = String.format(UPDATE_UNOCCUPIED_ROOMSTAT_TEMPLATE, getTableName());
		updateUnOccupiedMeetingRoomStatQuery = String.format(UPDATE_UNOCCUPIED_MEETINGROOMSTAT_TEMPLATE, getTableName());
		updateUserQuery = String.format(UPDATE_USER_TEMPLATE, getTableName(), getColumnColName());
		updateAccessTokenQuery = String.format(UPDATE_USER_ACCESS_TOKEN_TEMPLATE, getTableName());
		updateUserByMailQuery = String.format(UPDATE_USER_BY_MAIL_TEMPLATE, getTableName());
		updateGatewayStatusQuery = String.format(UPDATE_GATEWAY_STATUS_TEMPLATE, getTableName());
		updateAgentStatusQuery = String.format(UPDATE_AGENT_STATUS_TEMPLATE, getTableName());
		updateAgentMeetingRoomIdQuery = String.format(UPDATE_AGENT_MEETINGROOM_ID_TEMPLATE, getTableName());
		updateDashboardStatusQuery = String.format(UPDATE_DASHBOARD_STATUS_TEMPLATE, getTableName());
		updateGatewayCommandQuery = String.format(UPDATE_GATEWAY_COMMAND_TEMPLATE, getTableName());
		updateRoomQuery = String.format(UPDATE_ROOM_TEMPLATE, getTableName(), getColumnColName());
		updateMeetingRoomQuery = String.format(UPDATE_MEETINGROOM_TEMPLATE, getTableName(), getColumnColName());
		updateCountryQuery = String.format(UPDATE_COUNTRY_TEMPLATE, getTableName(), getColumnColName());
		updateRegionQuery = String.format(UPDATE_REGION_TEMPLATE, getTableName(), getColumnColName());
		updateCityQuery = String.format(UPDATE_CITY_TEMPLATE, getTableName(), getColumnColName());
		updateBuildingQuery = String.format(UPDATE_BUILDING_TEMPLATE, getTableName(), getColumnColName());
		updatePreferenceQuery = String.format(UPDATE_PREFERENCE_TEMPLATE, getTableName(), getColumnColName());
		updateTeachinStatusQuery = String.format(UPDATE_TEACHIN_STATUS_TEMPLATE, getTableName());
		updateTeachinDateQuery = String.format(UPDATE_TEACHIN_DATE_TEMPLATE, getTableName());
		updateAlertQuery = String.format(UPDATE_ALERT_TEMPLATE, getTableName());
		updateRoomStatusQuery = String.format(UPDATE_ROOM_STATUS_TEMPLATE, getTableName());// set also humidity, temperature & user_id if filled
		updateMeetingRoomStatusQuery = String.format(UPDATE_MEETINGROOM_STATUS_TEMPLATE, getTableName()); // set also oragnizzerlabel if filled
		updateSensorQuery = String.format(UPDATE_SENSOR_TEMPLATE, getTableName(), getColumnColName());
		updateSensorStatusQuery = String.format(UPDATE_SENSOR_STATUS_TEMPLATE, getTableName(), getColumnColName());
		updateSensorRoomIdQuery = String.format(UPDATE_SENSOR_ROOM_ID_TEMPLATE, getTableName());
		updateGatewayQuery = String.format(UPDATE_GATEWAY_TEMPLATE, getTableName());
		updateMeetingRoomGroupsConfigurationQuery = String.format(UPDATE_MEETINGROOM_GROUPS_CONFIGURATION_TEMPLATE, getTableName());
		updateAgentQuery = String.format(UPDATE_AGENT_TEMPLATE, getTableName());
		updateDashboardQuery = String.format(UPDATE_DASHBOARD_TEMPLATE, getTableName());
		
		// DELETE QUERIES ----------------------------------------------------------------------
		deleteQuery = String.format(REMOVE_TEMPLATE, getTableName());
		deleteAllQuery = String.format(REMOVE_ALL_TEMPLATE, getTableName());
		deleteByMacAddressQuery = String.format(REMOVE_BY_MAC_ADDRESS_TEMPLATE, getTableName());
		deleteByBuildingIdQuery = String.format(REMOVE_BY_BUILDING_ID_TEMPLATE, getTableName());
		deleteByBuildingIdAndFloorQuery = String.format(REMOVE_BY_BUILDING_ID_AND_FLOOR_TEMPLATE, getTableName());
		deleteByGatewayIdQuery = String.format(REMOVE_BY_GATEWAY_ID_TEMPLATE, getTableName());
		deleteBySensorIdQuery = String.format(REMOVE_BY_SENSOR_ID_TEMPLATE, getTableName());
		deleteByIdentifier = String.format(REMOVE_BY_IDENTIFIER_TEMPLATE, getTableName());
		deleteByDayQuery = String.format(REMOVE_BY_DAY_ROOM_DAILY_TEMPLATE, getTableName());
		deleteByDayQueryMeetingRoom = String.format(REMOVE_BY_DAY_MEETINGROOM_DAILY_TEMPLATE, getTableName());
		deleteByBeginOccupancyDateQuery = String.format(REMOVE_BY_DATE_ROOM_STATS_TEMPLATE, getTableName());
		deleteByBeginOccupancyDateQueryMeetingRoom = String.format(REMOVE_BY_DATE_MEETINGROOM_STATS_TEMPLATE, getTableName());
		deleteByRoomId = String.format(REMOVE_STAT_BY_ROOM_ID_TEMPLATE, getTableName());
		deleteByMeetingRoomId = String.format(REMOVE_STAT_BY_MEETINGROOM_ID_TEMPLATE, getTableName());
		deletePrefByCountryIdQuery = String.format(REMOVE_PREFERENCES_BY_COUNTRY_ID_TEMPLATE, getTableName());
		deletePrefByRegionIdQuery = String.format(REMOVE_PREFERENCES_BY_REGION_ID_TEMPLATE, getTableName());
		deletePrefByCityIdQuery = String.format(REMOVE_PREFERENCES_BY_CITY_ID_TEMPLATE, getTableName());
		deletePrefByBuildingIdQuery = String.format(REMOVE_PREFERENCES_BY_BUILDING_ID_TEMPLATE, getTableName());
		deletePrefByUserIdQuery = String.format(REMOVE_PREFERENCES_BY_USER_ID_TEMPLATE, getTableName());
		
		// COUNT QUERIES ----------------------------------------------------------------------
		countQuery = String.format(COUNT_TEMPLATE, getTableName());
		countSensorsByTypeAndRoomIdQuery = String.format(COUNT_SENSORS_BY_TYPE_AND_ROOM_ID_TEMPLATE, getTableName());
		countActiveUsersQuery = String.format(COUNT_ACTIVE_USERS_TEMPLATE, getTableName());
		countNbRoomsByTypeQuery = String.format(COUNT_ROOM_BY_TYPE_TEMPLATE, getTableName());
		countNbMeetingRoomsByTypeQuery = String.format(COUNT_MEETINGROOM_BY_TYPE_TEMPLATE, getTableName());
		
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
