package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.repository.data.RoomStatDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RoomStatDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class RoomStatDaoRepository extends DataRepository<RoomStatDao> implements RoomStatDaoOperations {

	private final Logger LOGGER = Logger.getLogger(RoomStatDaoRepository.class);
	
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
	protected String getTableName() {
		return RoomStatDaoMetadata.ROOM_STAT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RoomStatDaoMetadata.ROOM_STAT_ID_COL;
		
	}

	@Override
	public void forEach(DataExtractor dataExtractor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getRowColName() {
		// TODO Auto-generated method stub
		return null;
	}

	


}
