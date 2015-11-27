package com.orange.flexoffice.dao.common.repository.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.InitForTestDao;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;
import com.orange.flexoffice.dao.common.utils.TokenTools;

@Repository
public class InitTestRepository extends DataRepository<InitForTestDao>  {

	@Autowired
	private TokenTools tokenTools;
	
	public InitTestRepository() {
		super(InitForTestDao.class);
	}
	
	/**
	 * Script execute in BE GATEWAYAPI Class Test
	 */
	public boolean executeInitTestData() {
			String query = "DELETE FROM sensors";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM rooms";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM gateways";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM users";
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
					
			String sqlUser = "INSERT INTO users " +
					"(id, first_name, last_name, email, access_token, expired_token_date, password) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplateForTest.update(sqlUser, new Object[] {1, "user FirstName 1", "user LastName 1", "user Email 1", null, null, null});
			jdbcTemplateForTest.update(sqlUser, new Object[] {2, "user FirstName 2", "user LastName 2", "first.last5@test.com:test", "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0", tokenTools.createExpiredDate(), null});
			jdbcTemplateForTest.update(sqlUser, new Object[] {3, "user FirstName 3", "user LastName 3", "admin@oab.com", null, null, "flexoffice"});
			jdbcTemplateForTest.update(sqlUser, new Object[] {4, "user FirstName 4", "user LastName 4", "first.last1@test.com:pass", "Zmlyc3QubGFzdDFAdGVzdC5jb206cGFzczoxNDQ4NjEzNjU2MDk4", tokenTools.createExpiredDate(), null});
			
			String sqlRooms = "INSERT INTO rooms " +
					"(id, name, address, capacity, description, status, type, gateway_id, user_id) VALUES (?, ?, ?, ?, ?, CAST(? AS roomStatus), CAST(? AS roomType), ?, ?)";
			jdbcTemplateForTest.update(sqlRooms, new Object[] {1, "room 1", "04 rue de la chategneraie", 5, "room 1 desc", "FREE", "BOX", 1, 0});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {2, "room 2", "05 rue de la medina", 25, "room 2 desc", "RESERVED", "VIDEO_CONF", 1, 1});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {3, "room 3", "03 rue de l'amour", 5, "room 3 desc", "FREE", "BOX", 2, 4});
			return true;
	}


	@Override
	public void forEach(DataExtractor dataExtractor) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String getColumnColName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String getRowColName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
