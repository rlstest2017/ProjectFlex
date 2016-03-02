package com.orange.flexoffice.dao.common.repository.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.InitForTestDao;
import com.orange.flexoffice.dao.common.utils.TokenTools;

@Repository
public class InitTestRepository extends DataRepository<InitForTestDao>  {

	@Autowired
	private TokenTools tokenTools;
	
	public InitTestRepository() {
		super(InitForTestDao.class);
	}
	
	/**
	 * Script execute in Class Tests
	 */
	public boolean executeInitTestData() {
			String query = "DELETE FROM alerts";
			jdbcTemplateForTest.execute(query); 
			query = "DELETE FROM room_stats";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM sensors";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM rooms";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM buildings";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM cities";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM regions";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM countries";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM gateways";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM users";
			jdbcTemplateForTest.execute(query);
			
			String sqlGateways = "INSERT INTO gateways " +
						"(id, name, mac_address, description, status) VALUES (?, ?, ?, ?, CAST(? AS gatewayStatus))";
			jdbcTemplateForTest.update(sqlGateways, new Object[] {1, "gateway 1", "FF:EE:ZZ:AA:GG:PP", "gateway 1 test", "ONLINE"});
			jdbcTemplateForTest.update(sqlGateways, new Object[] {2, "gateway 2", "FF:TT:ZZ:AA:GG:PP", "gateway 2 test", "OFFLINE"});
			jdbcTemplateForTest.update(sqlGateways, new Object[] {3, "gateway 3", "AA:BB:CC:AA:GG:PP", "gateway 3 test", "ONLINE"});
			
			String sqlSensors = "INSERT INTO sensors " +
					 	            "(id, identifier, name, type, profile, description, status, room_id) VALUES (?, ?, ?, CAST(? AS sensorType), ?, ?, CAST(? AS sensorStatus), ?)";
			
			jdbcTemplateForTest.update(sqlSensors, new Object[] {1, "ident 1", "sensor 1", "MOTION_DETECTION", "as-07-01", "sensor 1 desc", "ONLINE", 1});
			jdbcTemplateForTest.update(sqlSensors, new Object[] {2, "ident 2", "sensor 2", "TEMPERATURE_HUMIDITY", "as-04-01", "sensor 2 desc", "OFFLINE", 1});
			jdbcTemplateForTest.update(sqlSensors, new Object[] {3, "ident 3", "sensor 3", "MOTION_DETECTION", "as-07-01", "sensor 3 desc", "UNSTABLE_RSSI", 2});
					
			String sqlUser = "INSERT INTO users " +
					"(id, first_name, last_name, email, access_token, expired_token_date, password, role) values (?, ?, ?, ?, ?, ?, ?, CAST(? AS userRole))";
			jdbcTemplateForTest.update(sqlUser, new Object[] {1, "user FirstName 1", "user LastName 1", "user Email 1", null, null, null, "DEFAULT" });
			jdbcTemplateForTest.update(sqlUser, new Object[] {2, "user FirstName 2", "user LastName 2", "first.last5@test.com:test", "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0", tokenTools.createExpiredDate(), null, "DEFAULT"});
			jdbcTemplateForTest.update(sqlUser, new Object[] {3, "user FirstName 3", "user LastName 3", "admin@oab.com", null, null, "flexoffice", "ADMIN"});
			jdbcTemplateForTest.update(sqlUser, new Object[] {4, "user FirstName 4", "user LastName 4", "first.last1@test.com:pass", "Zmlyc3QubGFzdDFAdGVzdC5jb206cGFzczoxNDQ4NjEzNjU2MDk4", tokenTools.createExpiredDate(), null, "DEFAULT"});
			
			String sqlCountries = "INSERT INTO countries " +
					"(id, name) VALUES (?, ?)";
			jdbcTemplateForTest.update(sqlCountries, new Object[] {1, "country 1"});
			jdbcTemplateForTest.update(sqlCountries, new Object[] {2, "country 2"});
			jdbcTemplateForTest.update(sqlCountries, new Object[] {3, "country 3"});
			
			String sqlRegions = "INSERT INTO regions " +
					"(id, name, country_id) VALUES (?, ?, ?)";
			jdbcTemplateForTest.update(sqlRegions, new Object[] {1, "region 1", 1});
			jdbcTemplateForTest.update(sqlRegions, new Object[] {2, "region 2", 1});
			jdbcTemplateForTest.update(sqlRegions, new Object[] {3, "region 3", 2});
			
			String sqlCities = "INSERT INTO cities " +
					"(id, name, region_id) VALUES (?, ?, ?)";
			jdbcTemplateForTest.update(sqlCities, new Object[] {1, "city 1", 1});
			jdbcTemplateForTest.update(sqlCities, new Object[] {2, "city 2", 1});
			jdbcTemplateForTest.update(sqlCities, new Object[] {3, "city 3", 2});
			
			String sqlBuildings = "INSERT INTO buildings " +
					"(id, name, address, city_id, nb_floors) VALUES (?, ?, ?, ?, ?)";
			jdbcTemplateForTest.update(sqlBuildings, new Object[] {1, "building 1", "04 rue de la Chategneraire 35000 Rennes", 1, 5});
			jdbcTemplateForTest.update(sqlBuildings, new Object[] {2, "building 2", "04 rue de la Chategneraire 35000 Rennes", 1, 10});
			jdbcTemplateForTest.update(sqlBuildings, new Object[] {3, "building 3", "04 rue de la Chategneraire 35000 Rennes", 2, 3});
			
			String sqlRooms = "INSERT INTO rooms " +
					"(id, name, address, capacity, description, status, type, gateway_id, user_id, building_id, floor) VALUES (?, ?, ?, ?, ?, CAST(? AS roomStatus), CAST(? AS roomType), ?, ?, ?, ?)";
			jdbcTemplateForTest.update(sqlRooms, new Object[] {1, "room 1", "04 rue de la chategneraie", 5, "room 1 desc", "FREE", "BOX", 1, null, 1, 3});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {2, "room 2", "05 rue de la medina", 25, "room 2 desc", "RESERVED", "VIDEO_CONF", 1, 1, 1, 0});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {3, "room 3", "03 rue de l'amour", 5, "room 3 desc", "FREE", "BOX", 2, 4, 2, 7});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {4, "room 4", "43 rue de l'amour", 35, "room 4 desc", "FREE", "BOX", 2, null, 3, 2});
			jdbcTemplateForTest.update(sqlRooms, new Object[] {5, "room 5", "41 rue de l'amour", 20, "room 5 desc", "FREE", "BOX", 2, null, 2, 3});
			
			String sqlRoomStats = "INSERT INTO room_stats " +
					"(id, room_id, user_id, begin_occupancy_date, end_occupancy_date, reservation_date, room_info) VALUES (?, ?, ?, CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS roomInfo))";
			jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 3, 4, "2015-12-08 18:56:25.620506", "2015-12-08 18:59:35.164569", "2015-12-02", "UNOCCUPIED" });
			jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 2, 4, "2015-12-09 09:20:32.676828", "2015-12-09 09:34:26.852377", "2015-11-27", "UNOCCUPIED"});
			jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 2, 4, null, null, "2015-12-03", "UNOCCUPIED" });
			jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 1, 4, null, null, "2015-12-04", "UNOCCUPIED"});
			jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 1, 4, "2016-01-19 07:04:35.051909", "2016-01-19 07:07:43.4994", "2015-11-03", "UNOCCUPIED" });
			
			
			return true;
	}

	/**
	 * Script execute in  Class Tests
	 */
	public boolean executeDropTables() {
			String query = "DELETE FROM sensors";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM rooms";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM gateways";
			jdbcTemplateForTest.execute(query);
			query = "DELETE FROM users";
			jdbcTemplateForTest.execute(query);

			return true;
	}

	public boolean initRoomStatsTableForUserUi() {
		String query = "DELETE FROM room_stats";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO room_stats " +
				"(id, room_id, user_id, reservation_date, room_info) VALUES (?, ?, ?, CAST(? AS timestamp), CAST(? AS roomInfo))";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 3, 4, "2015-12-02 11:35:44.704504", "RESERVED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 2, 4, "2015-11-27 11:35:44.704504", "RESERVED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 2, 4, "2015-12-03 11:35:44.704504", "RESERVED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 1, 4, "2015-12-04 11:35:44.704504", "RESERVED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 1, 4, "now()", "RESERVED" });
		
		return true;
	}

	public boolean initRoomStatsTableForAdminUi() {
		String query = "DELETE FROM room_stats";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO room_stats " +
				"(id, room_id, begin_occupancy_date, end_occupancy_date, room_info) VALUES (?, ?, CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS roomInfo))";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 1, "2015-12-01 11:35:44.704504", "2015-12-02 11:35:44.704504", "UNOCCUPIED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 3, "2015-12-10 08:10:00.704504", "2015-12-10 08:15:00.704504", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 2, "2015-12-10 11:30:00.704504", "2015-12-10 12:30:00.704504", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 2, "2015-12-10 14:00:00.704504", "2015-12-10 14:15:00.704504", "UNOCCUPIED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 3, "2015-12-09 11:35:44.704504", "now()", "UNOCCUPIED" });
		
		return true;
	}
	
	public boolean initRoomDailyOccupancyTable() {
		String query = "DELETE FROM room_daily_occupancy";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO room_daily_occupancy " +
				"(id, room_id, day, occupancy_duration) VALUES (?, ?, CAST(? AS timestamp), ?)";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 3, "2015-12-10 23:00:00.166785", 10343 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 4, "2015-12-10 23:00:00.166785", 28864 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 5, "2015-12-10 23:00:00.166785", 6263 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 3, "2015-12-11 23:00:00.085779", 11741 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 4, "2015-12-11 23:00:00.085779", 2313 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {6, 5, "2015-12-11 23:00:00.085779", 8443 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {7, 3, "2015-12-14 23:00:02.479225", 17744 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {8, 4, "2015-12-14 23:00:02.479225", 27499 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {9, 5, "2015-12-14 23:00:02.479225", 9800 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {10, 3, "2015-12-15 23:00:00.210389", 23275 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {11, 4, "2015-12-15 23:00:00.210389", 28750 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {12, 5, "2015-12-15 23:00:00.210389", 7531 });
						
		return true;
	}
	
	public boolean initRoomMonthlyOccupancyTable() {
		String query = "DELETE FROM room_daily_occupancy";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO room_daily_occupancy " +
				"(id, room_id, day, occupancy_duration) VALUES (?, ?, CAST(? AS timestamp), ?)";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 3, "2014-11-10 23:00:00.166785", 10343 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 4, "2014-11-12 23:00:00.166785", 28864 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 5, "2014-11-13 23:00:00.166785", 6263 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 3, "2015-01-01 23:00:00.085779", 11741 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 4, "2015-01-19 23:00:00.085779", 2313 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {6, 5, "2015-01-21 23:00:00.085779", 8443 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {7, 3, "2015-06-08 23:00:02.479225", 17744 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {8, 4, "2015-06-10 23:00:02.479225", 27499 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {9, 5, "2015-06-12 23:00:02.479225", 9800 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {10, 3, "2015-11-10 23:00:00.210389", 23275 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {11, 4, "2015-11-12 23:00:00.210389", 28750 });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {12, 5, "2015-11-13 23:00:00.210389", 7531 });
		
		return true;
	}
	
	
	public boolean initTeachinSensorsTable() {
		String query = "DELETE FROM teachin_sensors";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO teachin_sensors " +
				"(id, room_id, gateway_id, teachin_status, user_id) VALUES (?, ?, ?, CAST(? AS teachinStatus), ?)";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 1, 1, "INITIALIZING", 3 });
		
		return true;
	}
	
	public boolean setTeachinSensorsTable() {
		String query = "DELETE FROM teachin_sensors";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO teachin_sensors " +
				"(id, room_id, gateway_id, sensor_identifier, sensor_status, teachin_status, user_id) VALUES (?, ?, ?, ?,CAST(? AS sensorTeachinStatus) ,CAST(? AS teachinStatus), ?)";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 5, 1, null, null, "RUNNING", null });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, null, null, "test1", "NOT_PAIRED", null, null });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, null, null, "test2", "PAIRED_OK", null, null });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, null, null, "test3", "PAIRED_KO", null, null });
		
		return true;
	}
	
	@Override
	protected String getTableName() {
		return null;
	}

	@Override
	protected String getColumnColName() {
		return null;
	}

	@Override
	protected String getColName() {
		return null;
	}

}
