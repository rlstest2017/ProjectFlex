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

import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.object.RoomBuildingInfosDto;
import com.orange.flexoffice.dao.common.repository.data.RoomDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RoomDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage rooms table

last_measure_date is updated to now() for each access to method :
RoomDao updateRoomStatus(RoomDao data) throws DataAccessException
"update %s set status=CAST(:status AS roomstatus), temperature=:temperature, humidity=:humidity, last_measure_date=now(), user_id=:userId where id=:id"

/*---------------------------------------------------------------------------------------*/

@Repository
public class RoomDaoRepository extends DataRepository<RoomDao> implements RoomDaoOperations {

	
	public RoomDaoRepository() {
		super(RoomDao.class);
	}
	
	@Override
	public List<RoomDao> findAllRooms() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public List<RoomDao> findRoomsByCountryId(Long countryId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("countryId", countryId);
		return jdbcTemplate.query(
				findRoomsByCountryIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public List<RoomDao> findRoomsByRegionId(Long regionId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("regionId", regionId);
		return jdbcTemplate.query(
				findRoomsByRegionIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public List<RoomDao> findRoomsByCityId(Long cityId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("cityId", cityId);
		return jdbcTemplate.query(
				findRoomsByCityIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);	
	}

	@Override
	public List<RoomDao> findRoomsByBuildingId(Long buildingId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("buildingId", buildingId);
		return jdbcTemplate.query(
				findRoomsByBuildingIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);	
	}

	@Override
	public List<RoomDao> findRoomsByBuildingIdAndFloor(RoomBuildingInfosDto data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findRoomsByBuildingIdAndFloorQuery, 
				paramBean, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);	}
	
	@Override
	public RoomDao findByRoomId(Long roomId) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", roomId);
		return jdbcTemplate.queryForObject(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public List<RoomDao> findByGatewayId(Long gatewayId) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("gatewayId", gatewayId);
		return jdbcTemplate.query(
				findByColumnGatewayIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public RoomDao findByName(String name) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(
				findByColumnNameQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public RoomDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException{
		SqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.queryForObject(
				findByUserIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}
	
	@Override
	public RoomDao saveRoom(RoomDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveRoomQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public RoomDao updateRoom(RoomDao data) throws DataAccessException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateRoomQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		
		return data;
	}

	@Override
	public RoomDao updateRoomStatus(RoomDao data) throws DataAccessException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateRoomStatusQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}

	@Override
	public Long countRoomsByType(String type) {
		SqlParameterSource paramMap = new MapSqlParameterSource("type", type);
		return jdbcTemplate.queryForObject(
				countNbRoomsByTypeQuery, 
				paramMap, 
				Long.class
			);
	}
	
	@Override
	protected String getTableName() {
		return RoomDaoMetadata.ROOM_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RoomDaoMetadata.ROOM_ID_COL;
	}

	@Override
	protected String getColName() {
		return RoomDaoMetadata.ROOM_NAME_COL;
	}

	
}
