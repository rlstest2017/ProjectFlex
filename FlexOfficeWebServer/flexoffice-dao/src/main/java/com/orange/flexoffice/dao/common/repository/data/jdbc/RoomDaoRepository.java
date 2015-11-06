package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.repository.data.RoomDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RoomDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class RoomDaoRepository extends DataRepository<RoomDao> implements RoomDaoOperations {

	private final Logger LOGGER = Logger.getLogger(RoomDaoRepository.class);
	
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
	public List<RoomDao> findByRoomId(Long roomId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", roomId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
			);
	}

	@Override
	public List<RoomDao> findByGatewayId(Long gatewayId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("gatewayId", gatewayId);
		return jdbcTemplate.query(
				findByColumnGatewayIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RoomDao>(RoomDao.class)
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
	public void forEach(DataExtractor dataExtractor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getRowColName() {
		// TODO Auto-generated method stub
		return null;
	}


	

}
