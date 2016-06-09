package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomStatus;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomBuildingInfosDto;
import com.orange.flexoffice.dao.common.repository.data.MeetingRoomDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.MeetingRoomDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage meetingrooms table

last_measure_date is updated to now() for each access to method :
RoomDao updateRoomStatus(RoomDao data) throws DataAccessException
"update %s set status=CAST(:status AS meetingroomstatus), last_measure_date=now() where id=:id"

/*---------------------------------------------------------------------------------------*/

@Repository
public class MeetingRoomDaoRepository extends DataRepository<MeetingRoomDao> implements MeetingRoomDaoOperations {

	
	public MeetingRoomDaoRepository() {
		super(MeetingRoomDao.class);
	}
	
	@Override
	public List<MeetingRoomDao> findAllMeetingRooms() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}

	@Override
	public List<MeetingRoomDao> findMeetingRoomsByCountryId(Long countryId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("countryId", countryId);
		return jdbcTemplate.query(
				findMeetingRoomsByCountryIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}

	@Override
	public List<MeetingRoomDao> findMeetingRoomsByRegionId(Long regionId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("regionId", regionId);
		return jdbcTemplate.query(
				findMeetingRoomsByRegionIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}

	@Override
	public List<MeetingRoomDao> findMeetingRoomsByCityId(Long cityId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("cityId", cityId);
		return jdbcTemplate.query(
				findMeetingRoomsByCityIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);	
	}

	@Override
	public List<MeetingRoomDao> findMeetingRoomsByBuildingId(Long buildingId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("buildingId", buildingId);
		return jdbcTemplate.query(
				findMeetingRoomsByBuildingIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);	
	}

	@Override
	public List<MeetingRoomDao> findMeetingRoomsByBuildingIdAndFloor(MeetingRoomBuildingInfosDto data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findMeetingRoomsByBuildingIdAndFloorQuery, 
				paramBean, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);	}
	
	@Override
	public MeetingRoomDao findByMeetingRoomId(Long roomId) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", roomId);
		return jdbcTemplate.queryForObject(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}

	@Override
	public MeetingRoomDao findByAgentId(Long agentId) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("agentId", agentId);
		return jdbcTemplate.queryForObject(
				findByColumnAgentIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}

	@Override
	public MeetingRoomDao findByName(String name) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(
				findByColumnNameQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}
	
	@Override
	public MeetingRoomDao findByExternalId(String externalId) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("externalId", externalId);
		return jdbcTemplate.queryForObject(
				findMeetingRoomsByExternalIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}
	
	@Override
	public List<MeetingRoomDao> findMeetingRoomsInTimeout(String intervalTimeout) {
		SqlParameterSource paramMap = new MapSqlParameterSource("intervalTimeout", intervalTimeout);
		return jdbcTemplate.query(
				findMeetingRoomsInTimeoutQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDao>(MeetingRoomDao.class)
			);
	}
	
	@Override
	public MeetingRoomDao saveMeetingRoom(MeetingRoomDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveMeetingRoomQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public MeetingRoomDao updateMeetingRoom(MeetingRoomDao data) throws DataAccessException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateMeetingRoomQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		
		return data;
	}

	@Override
	public MeetingRoomDao updateMeetingRoomStatus(MeetingRoomDao data) throws DataAccessException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateMeetingRoomStatusQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}
	
	@Override
	public MeetingRoomDao updateMeetingRoomData(MeetingRoomDao data) throws DataAccessException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		if (E_MeetingRoomStatus.UNKNOWN.equals(data.getStatus())){
			jdbcTemplate.update(updateMeetingRoomDataForUnknownStatusQuery, paramBean, keyHolder);
		} else {
			jdbcTemplate.update(updateMeetingRoomDataQuery, paramBean, keyHolder);
		}
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}

	@Override
	public Long countMeetingRoomsByType(String type) {
		SqlParameterSource paramMap = new MapSqlParameterSource("type", type);
		return jdbcTemplate.queryForObject(
				countNbMeetingRoomsByTypeQuery, 
				paramMap, 
				Long.class
			);
	}
	
	@Override
	protected String getTableName() {
		return MeetingRoomDaoMetadata.MEETINGROOM_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return MeetingRoomDaoMetadata.MEETINGROOM_ID_COL;
	}

	@Override
	protected String getColName() {
		return MeetingRoomDaoMetadata.MEETINGROOM_NAME_COL;
	}
	
}
