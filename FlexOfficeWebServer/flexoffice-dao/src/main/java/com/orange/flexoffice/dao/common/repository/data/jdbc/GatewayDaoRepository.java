package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
	public GatewayDao findByGatewayId(Long gatewayId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", gatewayId);
		GatewayDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<GatewayDao>(GatewayDao.class)
			);
		return data;
	}
	
	@Override
	public GatewayDao findByMacAddress(String macAddress) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("macAddress", macAddress);
		GatewayDao data = null;
		data = jdbcTemplate.queryForObject(
				findByMacAddressQuery, 
				paramMap, 
				new BeanPropertyRowMapper<GatewayDao>(GatewayDao.class)
			);
		return data;
	}
	
	
//	@Override
//	public GatewayDao findByName(String name) {
//		SqlParameterSource paramMap = new MapSqlParameterSource("name", name);
//		GatewayDao data = null;
//		data = jdbcTemplate.queryForObject(
//				findByColumnNameQuery, 
//				paramMap, 
//				new BeanPropertyRowMapper<GatewayDao>(GatewayDao.class)
//			);
//		return data;
//	}
	
	@Override
	public GatewayDao saveGateway(GatewayDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveGatewayQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public GatewayDao updateGateway(GatewayDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateGatewayQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public GatewayDao updateGatewayStatus(GatewayDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		LOGGER.debug("Before execute jdbcTemplate update() method");
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateGatewayStatusQuery, paramBean, keyHolder);
		
		LOGGER.debug("After execute jdbcTemplate update() method");
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}

	@Override
	public void deleteByMacAddress(String macAddress) {
		SqlParameterSource paramMap = new MapSqlParameterSource("macAddress", macAddress);
		jdbcTemplate.update(deleteByMacAddressQuery, paramMap);
		
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
