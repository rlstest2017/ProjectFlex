package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

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
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

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
	public List<RoomStatDao> findLatestReservedRoomsByUserId(Long userId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(
				findLatestReservedRoomsQuery, 
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
	protected String getTableName() {
		return RoomStatDaoMetadata.ROOM_STAT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RoomStatDaoMetadata.ROOM_STAT_ID_COL;
		
	}

	@Override
	public void forEach(DataExtractor dataExtractor) {
	}

	@Override
	protected String getRowColName() {
		return null;
	}

}
