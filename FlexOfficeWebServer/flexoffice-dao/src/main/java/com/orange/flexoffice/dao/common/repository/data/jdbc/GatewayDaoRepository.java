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
	
	//------------------------------------------------------------------------------------------------------------------//
	//--------------------------------------  METHODS USED IN TESTS            -----------------------------------------//
	//------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------//
	/**
	 * Script execute in BE GATEWAYAPI Class Test
	 */
	public boolean executeGatewaysTestFile() {
			String query = "DELETE FROM sensors";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM rooms";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM gateways";
			jdbcTemplateForTest.execute(query);
			
			String sqlGateways = "INSERT INTO gateways " +
						"(id, name, mac_address, description, status) VALUES (?, ?, ?, ?, CAST(? AS gatewayStatus))";
			jdbcTemplateForTest.update(sqlGateways, new Object[] {1, "gateway 1", "FF:EE:ZZ:AA:GG:PP", "gateway 1 test", "ONLINE"});
			jdbcTemplateForTest.update(sqlGateways, new Object[] {2, "gateway 2", "FF:TT:ZZ:AA:GG:PP", "gateway 2 test", "OFFLINE"});
			
			String sqlSensors = "INSERT INTO sensors " +
					 	            "(id, identifier, name, type, profile, description, status, room_id) VALUES (?, ?, ?, CAST(? AS sensorType), ?, ?, CAST(? AS sensorStatus), ?)";
			
			jdbcTemplateForTest.update(sqlSensors, new Object[] { 1, "ident 1", "sensor 1", "MOTION_DETECTION", "as-07-01", "sensor 1 desc", "ONLINE", 1});
			jdbcTemplateForTest.update(sqlSensors, new Object[] {2, "ident 2", "sensor 2", "TEMPERATURE_HUMIDITY", "as-04-01", "sensor 2 desc", "OFFLINE", 1});
			jdbcTemplateForTest.update(sqlSensors, new Object[] {3, "ident 3", "sensor 3", "MOTION_DETECTION", "as-07-01", "sensor 3 desc", "UNSTABLE", 2});
					
			String sqlRooms = "INSERT INTO rooms " +
					"(id, name, address, capacity, description, status, type, gateway_id) VALUES (?, ?, ?, ?, ?, CAST(? AS roomStatus), CAST(? AS roomType), ?)";
			jdbcTemplateForTest.update(sqlRooms, new Object[] {1, "room 1", "04 rue de la chategneraie", 5, "room 1 desc", "FREE", "BOX", 1});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {2, "room 2", "05 rue de la medina", 25, "room 2 desc", "RESERVED", "VIDEO_CONF", 1});
	
			return true;
	}

}
