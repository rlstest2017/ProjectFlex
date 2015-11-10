package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.repository.data.GatewayDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.GatewayDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class GatewayDaoRepository extends DataRepository<GatewayDao> implements GatewayDaoOperations {

	private final Logger LOGGER = Logger.getLogger(GatewayDaoRepository.class);
	
	public GatewayDaoRepository() {
		super(GatewayDao.class);
	}
	
	@Override
	public List<GatewayDao> findAllGateways() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<GatewayDao>(GatewayDao.class)
			);
	}

	@Override
	public List<GatewayDao> findByGatewayId(Long gatewayId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", gatewayId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<GatewayDao>(GatewayDao.class)
			);
	}
	
		
	@Override
	protected String getTableName() {
		return GatewayDaoMetadata.GATEWAY_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return GatewayDaoMetadata.GATEWAY_ID_COL;
		
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
