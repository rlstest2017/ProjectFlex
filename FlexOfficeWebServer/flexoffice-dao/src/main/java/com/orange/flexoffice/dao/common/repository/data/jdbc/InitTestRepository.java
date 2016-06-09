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
		query = "DELETE FROM preferences";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM room_stats";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM meetingroom_stats";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM sensors";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM rooms";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM gateways";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM users";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM meetingrooms";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM dashboards";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM agents";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM meetingroom_groups_configuration";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM buildings";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM cities";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM regions";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM countries";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM configuration";
		jdbcTemplateForTest.execute(query);
		
		String sqlGateways = "INSERT INTO gateways " +
					"(id, name, mac_address, description, status) VALUES (?, ?, ?, ?, CAST(? AS gatewayStatus))";
		jdbcTemplateForTest.update(sqlGateways, new Object[] {1, "gateway 1", "ff:ee:zz:aa:gg:pp", "gateway 1 test", "ONLINE"});
		jdbcTemplateForTest.update(sqlGateways, new Object[] {2, "gateway 2", "ff:tt:zz:aa:gg:pp", "gateway 2 test", "OFFLINE"});
		jdbcTemplateForTest.update(sqlGateways, new Object[] {3, "gateway 3", "aa:bb:cc:aa:gg:pp", "gateway 3 test", "ONLINE"});
		
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
		jdbcTemplateForTest.update(sqlBuildings, new Object[] {2, "building 2", "05 rue de la medina 35000 Rennes", 1, 10});
		jdbcTemplateForTest.update(sqlBuildings, new Object[] {3, "building 3", "03 rue de l'amour 35000 Rennes", 2, 3});
		
		String sqlRooms = "INSERT INTO rooms " +
				"(id, name, capacity, description, status, type, gateway_id, user_id, building_id, floor) VALUES (?, ?, ?, ?, CAST(? AS roomStatus), CAST(? AS roomType), ?, ?, ?, ?)";
		jdbcTemplateForTest.update(sqlRooms, new Object[] {1, "room 1", 5, "room 1 desc", "FREE", "BOX", 1, null, 1, 3});
		jdbcTemplateForTest.update(sqlRooms, new Object[] {2, "room 2", 25, "room 2 desc", "RESERVED", "VIDEO_CONF", 1, 1, 1, 0});
		jdbcTemplateForTest.update(sqlRooms, new Object[] {3, "room 3", 5, "room 3 desc", "RESERVED", "BOX", 2, 4, 2, 7});
		jdbcTemplateForTest.update(sqlRooms, new Object[] {4, "room 4", 35, "room 4 desc", "FREE", "BOX", 2, null, 1, 3});
		jdbcTemplateForTest.update(sqlRooms, new Object[] {5, "room 5", 20, "room 5 desc", "FREE", "BOX", 2, null, 2, 3});
		
		String sqlRoomStats = "INSERT INTO room_stats " +
				"(id, room_id, user_id, begin_occupancy_date, end_occupancy_date, reservation_date, room_info) VALUES (?, ?, ?, CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS roomInfo))";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 3, 4, "2015-12-08 18:56:25.620506", "2015-12-08 18:59:35.164569", "2015-12-02", "OCCUPIED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 2, 4, "2015-12-09 09:20:32.676828", "2015-12-09 09:34:26.852377", "2015-11-27", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 2, 4, null, null, "2015-12-03", "UNOCCUPIED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 1, 4, null, null, "2015-12-04", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 1, 4, "2016-01-19 07:04:35.051909", "2016-01-19 07:07:43.4994", "2015-11-03", "OCCUPIED" });
		
		String sqlMeetingRoomStats = "INSERT INTO meetingroom_stats " +
				"(id, meetingroom_id, begin_occupancy_date, end_occupancy_date, meetingroom_info) VALUES (?, ?, CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS meetingroomInfo))";
		jdbcTemplateForTest.update(sqlMeetingRoomStats, new Object[] {1, 3, "2015-12-08 18:56:25.620506", "2015-12-08 18:59:35.164569", "OCCUPIED" });
		jdbcTemplateForTest.update(sqlMeetingRoomStats, new Object[] {2, 2, "2015-12-09 09:20:32.676828", "2015-12-09 09:34:26.852377", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlMeetingRoomStats, new Object[] {3, 2, null, null, "UNOCCUPIED" });
		jdbcTemplateForTest.update(sqlMeetingRoomStats, new Object[] {4, 1, null, null, "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlMeetingRoomStats, new Object[] {5, 1, "2016-01-19 07:04:35.051909", "2016-01-19 07:07:43.4994", "OCCUPIED" });
		
		String sqlAgents = "INSERT INTO agents " +
				"(id, name, mac_address, description, status, meetingroom_id, last_measure_date, command) VALUES (?, ?, ?, ?, CAST(? AS agentStatus), ?, CAST(? AS timestamp), CAST(? AS commandModel))";
		jdbcTemplateForTest.update(sqlAgents, new Object[] {1, "agent 1", "ff:ee:zz:aa:gg:pp", "agent 1 test", "ONLINE", 1, "2016-01-21 07:04:35.051909", "ECONOMIC"});
		jdbcTemplateForTest.update(sqlAgents, new Object[] {2, "agent 2", "ff:tt:zz:aa:gg:pp", "agent 2 test", "OFFLINE", 0, "2016-01-22 07:04:35.051909", "ECONOMIC"});
		jdbcTemplateForTest.update(sqlAgents, new Object[] {3, "agent 3", "aa:bb:cc:aa:gg:pp", "agent 3 test", "ONLINE", 3, "2016-01-23 07:04:35.051909", "STANDBY"});
		jdbcTemplateForTest.update(sqlAgents, new Object[] {4, "agent 4", "ff:ts:zz:aa:gg:pp", "agent 4 test", "OFFLINE", 4, "2016-01-24 07:04:35.051909", "ECONOMIC"});
		jdbcTemplateForTest.update(sqlAgents, new Object[] {5, "agent 5", "aa:bs:cc:aa:gg:pp", "agent 5 test", "ONLINE", 0, "2016-01-25 07:04:35.051909", "ECONOMIC"});
		
		String sqlMeetingRooms = "INSERT INTO meetingrooms " +
				"(id, external_id, name, organizerLabel, capacity, description, status, type, agent_id, building_id, floor, start_date, end_date, last_measure_date) VALUES"
				+ " (?, ?, ?, ?,  ?, ?, CAST(? AS meetingroomStatus), CAST(? AS meetingroomType), ?, ?, ?, CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS timestamp))";
		jdbcTemplateForTest.update(sqlMeetingRooms, new Object[] {1, "1@id.fr", "meeting room 1", null, 5, "meeting room 1 desc", "FREE", "BOX", 1, 1, 1, "2016-01-20 07:04:35.051909", "2016-01-20 09:04:35.051909", "2016-01-19 10:04:35.051909"});
		jdbcTemplateForTest.update(sqlMeetingRooms, new Object[] {2, "2@id.fr", "meeting room 2", "organizer 2", 25, "meeting room 2 desc", "OCCUPIED", "VIDEO_CONF", 2, 2, 1, "2016-01-19 07:04:35.051909", "2016-01-19 08:04:35.051909", "2016-01-19 17:04:35.051909"});
		jdbcTemplateForTest.update(sqlMeetingRooms, new Object[] {3, "3@id.fr", "meeting room 3", "organizer 3", 5, "meeting room 3 desc", "FREE", "BOX", 3, 1, 4, "2016-01-22 07:04:35.051909", "2016-01-22 09:04:35.051909", "2016-01-22 16:04:35.051909"});
		jdbcTemplateForTest.update(sqlMeetingRooms, new Object[] {4, "4@id.fr", "meeting room 4", "organizer 4", 35, "meeting room 4 desc", "FREE", "BOX", 4, 2, 1, "2016-01-23 07:04:35.051909", "2016-01-23 10:04:35.051909", "2016-01-23 14:04:35.051909"});
		jdbcTemplateForTest.update(sqlMeetingRooms, new Object[] {5, "5@id.fr", "meeting room 5", null, 20, "meeting room 5 desc", "FREE", "BOX", 5, 1, 1, "2016-01-14 07:04:35.051909", "2016-01-14 09:04:35.051909","2016-01-14 10:04:35.051909"});
		
		String sqlDashboards = "INSERT INTO dashboards " +
				"(id, mac_address, name, description, status, last_measure_date, city_id, building_id, floor, command) VALUES (?, ?, ?, ?, CAST(? AS dashboardStatus), CAST(? AS timestamp), ?, ?, ?, CAST(? AS commandModel))";
		jdbcTemplateForTest.update(sqlDashboards, new Object[] {1, "ff:ee:zz:aa:gg:pp", "dashboard 1", "dashboard 1 test", "ONLINE", "2016-01-21 07:04:35.051909", 1, 1, null, "ECONOMIC"});
		jdbcTemplateForTest.update(sqlDashboards, new Object[] {2, "ff:tt:zz:aa:gg:pp", "dashboard 2", "dashboard 2 test", "OFFLINE", "2016-01-22 07:04:35.051909", 1, 1, 0, "ECONOMIC"});
		jdbcTemplateForTest.update(sqlDashboards, new Object[] {3, "aa:bb:cc:aa:gg:pp", "dashboard 3", "dashboard 3 test", "ONLINE", "2016-01-23 07:04:35.051909", 1, 1, 0, "ECONOMIC"});
		jdbcTemplateForTest.update(sqlDashboards, new Object[] {4, "ff:ts:zz:aa:gg:pp", "dashboard 4", "dashboard 4 test", "OFFLINE", "2016-01-24 07:04:35.051909", 2, 1, 0, "STANDBY"});
		jdbcTemplateForTest.update(sqlDashboards, new Object[] {5, "aa:bs:cc:aa:gg:pp", "dashboard 5", "dashboard 5 test", "ONLINE", "2016-01-25 07:04:35.051909", 2, 1, 0, "ECONOMIC"});

		String sqlMeetinRoomGroupsConfiguration = "INSERT INTO meetingroom_groups_configuration (id, building_id, floor, meetingroom_group_id) VALUES (?, ?, ?, ?)";
		jdbcTemplateForTest.update(sqlMeetinRoomGroupsConfiguration, new Object[] {1, 1, 0, "1_0"});
		jdbcTemplateForTest.update(sqlMeetinRoomGroupsConfiguration, new Object[] {2, 1, 1, "1_1"});
		jdbcTemplateForTest.update(sqlMeetinRoomGroupsConfiguration, new Object[] {3, 1, 2, "1_2"});
		jdbcTemplateForTest.update(sqlMeetinRoomGroupsConfiguration, new Object[] {4, 2, 0, "2_0"});
		jdbcTemplateForTest.update(sqlMeetinRoomGroupsConfiguration, new Object[] {5, 2, 1, "2_1"});
		
		String sqlConfiguration = "INSERT INTO configuration (id, key, value, description) VALUES (?, ?, ?, ?)";
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {1, "LAST_CONNECTION_DURATION", "15", "This data is in days"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {2, "OCCUPANCY_TIMEOUT", "3", "This data is in minutes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {3, "BOOKING_DURATION", "300", "This data is in secondes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {4, "LAST_RESERVED_COUNT", "0", "This data is number of rooms to return, 0 means all rooms"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {5, "DATE_BEGIN_DAY", "07:30", "This data is in format hour:minutes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {6, "DATE_END_DAY", "20:00", "This data is in format hour:minutes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {7, "TEACHIN_TIMEOUT", "15", "This data is in minutes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {8, "KEEP_STAT_DATA_IN_DAYS", "365", "This data is in days"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {9, "THRESHOLD_ENABLED_ADVANCEDRESEARCH_OF_ROOMS", "1", "more than this parameter value, the advanced research of rooms is activated"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {10, "AGENT_STATUS_TIMEOUT", "3", "This data is in minutes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {11, "DASHBOARD_STATUS_TIMEOUT", "3", "This data is in minutes"});
		jdbcTemplateForTest.update(sqlConfiguration, new Object[] {12, "MEETINGROOM_STATUS_TIMEOUT", "3", "This data is in minutes"});
		
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
		query = "DELETE FROM meetingrooms";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM dashboards";
		jdbcTemplateForTest.execute(query);
		query = "DELETE FROM agents";
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
	
	public boolean initMeetingRoomStatsTableForAdminUi() {
		String query = "DELETE FROM meetingroom_stats";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO meetingroom_stats " +
				"(id, meetingroom_id, begin_occupancy_date, end_occupancy_date, meetingroom_info) VALUES (?, ?, CAST(? AS timestamp), CAST(? AS timestamp), CAST(? AS meetingRoomInfo))";
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {1, 1, "2015-12-01 11:35:44.704504", "2015-12-02 11:35:44.704504", "UNOCCUPIED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {2, 3, "2015-12-10 08:10:00.704504", "2015-12-10 08:15:00.704504", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {3, 2, "2015-12-10 11:30:00.704504", "2015-12-10 12:30:00.704504", "UNOCCUPIED"});
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {4, 2, "2015-12-10 14:00:00.704504", "2015-12-10 14:15:00.704504", "UNOCCUPIED" });
		jdbcTemplateForTest.update(sqlRoomStats, new Object[] {5, 3, "2015-12-09 11:35:44.704504", "now()", "UNOCCUPIED" });
		
		return true;
	}
	
	public boolean initMeetingRoomDailyOccupancyTable() {
		String query = "DELETE FROM meetingroom_daily_occupancy";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO meetingroom_daily_occupancy " +
				"(id, meetingroom_id, day, occupancy_duration) VALUES (?, ?, CAST(? AS timestamp), ?)";
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
	
	public boolean initMeetingRoomMonthlyOccupancyTable() {
		String query = "DELETE FROM meetingroom_daily_occupancy";
		jdbcTemplateForTest.execute(query);
		String sqlRoomStats = "INSERT INTO meetingroom_daily_occupancy " +
				"(id, meetingroom_id, day, occupancy_duration) VALUES (?, ?, CAST(? AS timestamp), ?)";
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
