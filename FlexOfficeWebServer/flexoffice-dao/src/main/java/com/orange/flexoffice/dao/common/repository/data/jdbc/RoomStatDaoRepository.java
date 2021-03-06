package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.Date;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.repository.data.RoomStatDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RoomStatDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage room_stats table

Si réservation via userUI "j y vais" alors le champ reservation_date=now() & room_info=RESERVED
Si annulation via userUIvia "je n y vais pas" alors room_info=CANCELED et les autres champs restent vides !!! 

Si room occupé grâce à une détection gatewayApi alors le champ begin_occupancy_date=now() & room_info="OCCUPIED"
Au bout de OCCUPANCY_TIMEOUT parameter in configuration table, si détection d'aucune présence alors
end_occupancy_date=now() & room_info="UNOCCUPIED"

Si la salle est occupée après une réservation alors le champ is_reservation_honored=true

------------------------------------------------------------------------------------------*/

@Repository
public class RoomStatDaoRepository extends DataRepository<RoomStatDao> implements RoomStatDaoOperations {

	public RoomStatDaoRepository() {
		super(RoomStatDao.class);
	}
	
	
	@Override
	public List<RoomStatDao> findAllRoomStats() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomStatDao>(RoomStatDao.class)
			);
	}
	
	@Override
	public List<RoomStatDao> findAllOccupiedDailyRoomStats(RoomStatDao data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findAllDailyQuery, 
				paramBean, 
				new BeanPropertyRowMapper<RoomStatDao>(RoomStatDao.class)
			);
	}
	
	@Override
	public List<RoomStatDao> findLatestReservedRoomsByUserId(Long userId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(
				findLatestReservedRoomsQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomStatDao>(RoomStatDao.class)
			);
	}
	
	@Override
	public RoomStatDao findbyRoomId(RoomStatDao data) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.queryForObject(
				findRoomStatByRoomIdQuery, 
				paramBean, 
				new BeanPropertyRowMapper<RoomStatDao>(RoomStatDao.class)
			);
	}
	
	@Override
	public List<RoomStatDao> findbyRoomInfo(RoomStatDao data) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("roomInfo", data.getRoomInfo());
		return jdbcTemplate.query(
				findRoomStatByRoomInfoQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomStatDao>(RoomStatDao.class)
			);
	}
	
	@Override
	public RoomStatDao saveReservedRoomStat(RoomStatDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveReservedRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}
	
	@Override
	public RoomStatDao saveOccupiedRoomStat(RoomStatDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveOccupiedRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}

	
	@Override
	public RoomStatDao updateReservedRoomStat(RoomStatDao data) {
	KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateReservedRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}

	@Override
	public RoomStatDao updateRoomStatById(RoomStatDao data) {
	KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateRoomStatByIdQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}
	
	@Override
	public RoomStatDao updateBeginOccupancyDate(RoomStatDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateOccupiedRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}

	@Override
	public RoomStatDao updateEndOccupancyDate(RoomStatDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateUnOccupiedRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}
	
	@Override
	public void deleteByBeginOccupancyDate(Date date) {
		SqlParameterSource paramMap = new MapSqlParameterSource("date", date);
		jdbcTemplate.update(deleteByBeginOccupancyDateQuery, paramMap);
	}
	
	@Override
	public void deleteByRoomId(Long roomId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("roomId", roomId);
		jdbcTemplate.update(deleteByRoomId, paramMap);
	}
	
	@Override
	protected String getTableName() {
		return RoomStatDaoMetadata.ROOM_STAT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RoomStatDaoMetadata.ROOM_STAT_ID_COL;
	}

	@Override
	protected String getColName() {
		return RoomStatDaoMetadata.ROOM_STAT_ID_COL;
	}
	
}
