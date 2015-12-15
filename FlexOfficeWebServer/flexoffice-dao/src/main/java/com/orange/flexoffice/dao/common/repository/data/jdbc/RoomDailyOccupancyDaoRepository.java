package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.object.RoomDailyOccupancyDto;
import com.orange.flexoffice.dao.common.model.object.RoomDailyTypeDto;
import com.orange.flexoffice.dao.common.repository.data.RoomDailyOccupancyDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RoomDailyOccupancyDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class RoomDailyOccupancyDaoRepository extends DataRepository<RoomDailyOccupancyDao> implements RoomDailyOccupancyDaoOperations {

	public RoomDailyOccupancyDaoRepository() {
		super(RoomDailyOccupancyDao.class);
	}
	
	@Override
	public List<RoomDailyOccupancyDao> findAllRoomsDailyOccupancy() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllRoomDailyQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDailyOccupancyDao>(RoomDailyOccupancyDao.class)
			);
	}
	
	@Override
	public List<RoomDailyOccupancyDao> findRequestedRoomsDailyOccupancy(RoomDailyOccupancyDto data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findRequestedDailyQuery, 
				paramBean, 
				new BeanPropertyRowMapper<RoomDailyOccupancyDao>(RoomDailyOccupancyDao.class)
			);
	}

	@Override
	public List<RoomDailyTypeDto> findRoomsDailyAndType() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllDailyAndTypesQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDailyTypeDto>(RoomDailyTypeDto.class)
			);
	}
	
	@Override
	public RoomDailyOccupancyDao saveRoomDaily(RoomDailyOccupancyDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveRoomDailyQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}
	
	@Override
	protected String getTableName() {
		return RoomDailyOccupancyDaoMetadata.ROOM_DAILY_OCCUPANCY_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RoomDailyOccupancyDaoMetadata.ROOM_DAILY_OCCUPANCY_ROOM_ID_COL;
		
	}

	@Override
	public void forEach(DataExtractor dataExtractor) {
	}

	@Override
	protected String getRowColName() {
		return null;
	}



}
