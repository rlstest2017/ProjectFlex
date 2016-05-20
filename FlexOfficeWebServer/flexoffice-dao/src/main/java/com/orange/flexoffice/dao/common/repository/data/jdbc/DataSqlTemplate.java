package com.orange.flexoffice.dao.common.repository.data.jdbc;

public final class DataSqlTemplate {
	
	public static final String FIND_ONE_TEMPLATE = 
			"select * from %s where id=:id";
	public static final String FIND_BY_COL_ID_TEMPLATE = 
			"select * from %s where %s=:columnId";
	public static final String FIND_PREFERENCE_BY_USER_ID_TEMPLATE = 
			"select * from %s where %s=:columnId";
	public static final String FIND_REGION_DTO_BY_COL_ID_TEMPLATE = 
			"select regions.id, regions.name, countries.id as country_id, countries.name as country_name From regions, countries where regions.country_id=countries.id and regions.id=:columnId";
	public static final String FIND_CITY_DTO_BY_COL_ID_TEMPLATE = 
			"select cities.id, cities.name, regions.id as region_id, regions.name as region_name, countries.id as country_id, countries.name as country_name From cities, regions, countries where cities.region_id=regions.id and regions.country_id=countries.id and cities.id=:columnId";
	public static final String FIND_BUILDING_DTO_BY_COL_ID_TEMPLATE = 
			"select buildings.id, buildings.name, buildings.address, cities.id as city_id, cities.name as city_name, regions.id as region_id, regions.name as region_name, countries.id as country_id, countries.name as country_name, buildings.nb_floors From buildings, cities, regions, countries where buildings.city_id=cities.id and cities.region_id=regions.id and regions.country_id=countries.id and buildings.id=:columnId";
	public static final String FIND_BY_TEACHIN_STATUS_TEMPLATE = 
			"select * from %s where teachin_status is not null";
	public static final String FIND_BY_COL_KEY_TEMPLATE = 
			"select * from %s where key=:key";
	public static final String FIND_ROOMSTAT_BY_ROOMID_TEMPLATE = 
			"select * from %s where room_id=:roomId and room_info=CAST(:roomInfo AS roomInfo)";
	public static final String FIND_MEETINGROOMSTAT_BY_MEETINGROOMID_TEMPLATE = 
			"select * from %s where meetingroom_id=:meetingRoomId and meetingroom_info=CAST(:meetingRoomInfo AS meetingRoomInfo)";
	public static final String FIND_ROOMSTAT_BY_ROOMINFO_TEMPLATE = 
			"select * from %s where room_info=CAST(:roomInfo AS roomInfo)";
	public static final String FIND_MEETINGROOMSTAT_BY_MEETINGROOMINFO_TEMPLATE = 
			"select * from %s where meetingroom_info=CAST(:meetingRoomInfo AS meetingRoomInfo)";
	public static final String FIND_BY_MAC_ADDRESS_TEMPLATE = 
			"select * from %s where mac_address=:macAddress";
	public static final String FIND_BY_MEETINGROOM_ID_TEMPLATE = 
			"select * from %s where meetingroom_id=:meetingRoomId";
	public static final String FIND_BY_COL_GATEWAY_ID_TEMPLATE = 
			"select * from %s where gateway_id=:gatewayId";
	public static final String FIND_BY_COL_AGENT_ID_TEMPLATE = 
			"select * from %s where agent_id=:agentId";
	public static final String FIND_BY_COL_DASHBOARD_ID_TEMPLATE = 
			"select * from %s where dashboard_id=:dashboardId";
	public static final String FIND_BY_COL_COUNTRY_ID_TEMPLATE = 
			"select * from %s where country_id=:countryId";
	public static final String FIND_ROOMS_BY_COUNTRY_ID_TEMPLATE = 
			"select distinct rooms.* From rooms, buildings, cities, regions, countries where rooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=regions.id and regions.country_id=:countryId order by rooms.name";
	public static final String FIND_ROOMS_BY_REGION_ID_TEMPLATE = 
			"select distinct rooms.* From rooms, buildings, cities where rooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=:regionId order by rooms.name";
	public static final String FIND_ROOMS_BY_CITY_ID_TEMPLATE = 
			"select distinct rooms.* From rooms, buildings where rooms.building_id=buildings.id and buildings.city_id=:cityId order by rooms.name";
	public static final String FIND_ROOMS_BY_BUILDING_ID_TEMPLATE = 
			"select distinct rooms.* From rooms where rooms.building_id=:buildingId order by rooms.name";
	public static final String FIND_ROOMS_BY_BUILDING_ID_AND_FLOOR_TEMPLATE = 
			"select distinct rooms.* From rooms where rooms.building_id=:buildingId and rooms.floor=:floor order by rooms.name";
	public static final String FIND_MEETINGROOMS_BY_COUNTRY_ID_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms, buildings, cities, regions, countries where meetingrooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=regions.id and regions.country_id=:countryId order by meetingrooms.name";
	public static final String FIND_MEETINGROOMS_BY_REGION_ID_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms, buildings, cities where meetingrooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=:regionId order by meetingrooms.name";
	public static final String FIND_MEETINGROOMS_BY_CITY_ID_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms, buildings where meetingrooms.building_id=buildings.id and buildings.city_id=:cityId order by meetingrooms.name";
	public static final String FIND_MEETINGROOMS_BY_BUILDING_ID_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms where meetingrooms.building_id=:buildingId order by meetingrooms.name";
	public static final String FIND_MEETINGROOMS_BY_BUILDING_ID_AND_FLOOR_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms where meetingrooms.building_id=:buildingId and meetingrooms.floor=:floor order by meetingrooms.name";
	public static final String FIND_MEETINGROOMS_BY_EXTERNAL_ID_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms where meetingrooms.external_id=:externalId";
	public static final String FIND_MEETINGROOMS_IN_TIMEOUT_TEMPLATE = 
			"select distinct meetingrooms.* From meetingrooms where last_measure_date < current_timestamp - cast(:intervalTimeout|| 'MINUTE' as interval)";
	public static final String FIND_AGENTS_IN_TIMEOUT_TEMPLATE = 
			"select distinct agents.* From agents where last_measure_date < current_timestamp - cast(:intervalTimeout|| 'MINUTE' as interval)";
	public static final String FIND_DASHBOARDS_IN_TIMEOUT_TEMPLATE = 
			"select distinct dashboards.* From dashboards where last_measure_date < current_timestamp - cast(:intervalTimeout|| 'MINUTE' as interval)";
	public static final String FIND_REGIONS_HAVE_ROOMS_BY_COUNTRY_ID_TEMPLATE = 
			"select distinct regions.id, regions.name From rooms, buildings, cities, regions, countries where rooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=regions.id and regions.country_id=countries.id and country_id=:countryId";
	public static final String FIND_CITIES_HAVE_ROOMS_BY_REGION_ID_TEMPLATE = 
			"select distinct cities.id, cities.name From rooms, buildings, cities, regions where rooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=regions.id and region_id=:regionId";
	public static final String FIND_BUILDINGS_HAVE_ROOMS_BY_CITY_ID_TEMPLATE = 
			"select distinct buildings.id, buildings.name, buildings.nb_floors From rooms, buildings, cities where rooms.building_id=buildings.id and buildings.city_id=cities.id and city_id=:cityId";
	public static final String FIND_BY_COL_REGION_ID_TEMPLATE = 
			"select * from %s where region_id=:regionId";
	public static final String FIND_BY_COL_CITY_ID_TEMPLATE = 
			"select * from %s where city_id=:cityId";
	public static final String FIND_BY_COL_BUILDING_ID_TEMPLATE = 
			"select * from %s where building_id=:buildingId";
	public static final String FIND_BY_COL_BUILDING_ID_AND_FLOOR_TEMPLATE = 
			"select * from %s where building_id=:buildingId and floor=:floor";
	public static final String FIND_BY_COL_SENSOR_ID_TEMPLATE = 
			"select * from %s where sensor_id=:sensorId";
	public static final String FIND_BY_COL_NAME_TEMPLATE = 
			"select * from %s where name=:name";
	public static final String FIND_BY_COL_MAIL_TEMPLATE = 
			"select * from %s where email=:email";
	public static final String FIND_BY_COL_MAIL_PASSWORD_TEMPLATE = 
			"select * from %s where email=:email and password=:password and role=CAST(:role AS userRole)";
	public static final String FIND_BY_COL_ACCESS_TOKEN_TEMPLATE = 
			"select * from %s where access_token=:accessToken";
	public static final String FIND_BY_COL_ROOM_ID_TEMPLATE = 
			"select * from %s where room_id=:roomId";
	public static final String FIND_BY_COL_ROOM_ID_OCCUPIED_INFO_TEMPLATE = 
			"select * from %s where room_id=:roomId and occupancy_info='OCCUPIED'";
	public static final String FIND_BY_COL_USER_ID_TEMPLATE = 
			"select * from %s where user_id=:userId";
	public static final String FIND_BY_SENSOR_IDENTIFIER_TEMPLATE = 
			"select * from %s where sensor_identifier=:identifier";
	public static final String FIND_BY_IDENTIFIER_TEMPLATE = 
			"select * from %s where identifier=:identifier";
	public static final String FIND_ALL_TEMPLATE = 
			"select * from %s order by %s";
	public static final String FIND_COUNTRIES_HAVE_ROOMS_TEMPLATE = 
			"select distinct countries.id, countries.name From rooms, buildings, cities, regions, countries where rooms.building_id=buildings.id and buildings.city_id=cities.id and cities.region_id=regions.id and regions.country_id=countries.id";
	public static final String FIND_ALL_ROOM_DAILY_TEMPLATE = 
			"select * from %s order by room_id";
	public static final String FIND_ALL_MEETINGROOM_GROUPS_CONFIGURATION_TEMPLATE = 
			"select * from %s order by building_id";
	public static final String FIND_ALL_MEETINGROOM_DAILY_TEMPLATE = 
			"select * from %s order by meetingroom_id";
	public static final String FIND_REQUESTED_ROOM_DAILY_AND_TYPE_TEMPLATE = 
			"select room_daily_occupancy.day, room_daily_occupancy.occupancy_duration, rooms.type From room_daily_occupancy, rooms where rooms.id=room_daily_occupancy.room_id and room_daily_occupancy.day >:fromDate and room_daily_occupancy.day <:toDate order by room_daily_occupancy.day";
	public static final String FIND_REQUESTED_MEETINGROOM_DAILY_AND_TYPE_TEMPLATE = 
			"select meetingroom_daily_occupancy.day, meetingroom_daily_occupancy.occupancy_duration, meetingrooms.type From meetingroom_daily_occupancy, meetingrooms where meetingrooms.id=meetingroom_daily_occupancy.meetingroom_id and meetingroom_daily_occupancy.day >:fromDate and meetingroom_daily_occupancy.day <:toDate order by meetingroom_daily_occupancy.day";
	public static final String FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE = 
			"select * from %s where room_info=CAST(:roomInfo AS roomInfo) and begin_occupancy_date >:beginOccupancyDate and end_occupancy_date <:endOccupancyDate order by room_id";
	public static final String FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE_MEETINGROOM = 
			"select * from %s where meetingroom_info=CAST(:meetingroomInfo AS meetingRoomInfo) and begin_occupancy_date >:beginOccupancyDate and end_occupancy_date <:endOccupancyDate order by meetingroom_id";
	public static final String FIND_ALL_REGIONS_SUMMARY_TEMPLATE = 
	"select regions.id, regions.name, countries.name as country_name From regions, countries where regions.country_id=countries.id";
	public static final String FIND_ALL_CITIES_SUMMARY_TEMPLATE = 
	"select cities.id, cities.name, regions.name as region_name, countries.name as country_name From cities, regions, countries where cities.region_id=regions.id and regions.country_id=countries.id";
	public static final String FIND_ALL_BUILDINGS_SUMMARY_TEMPLATE = 
	"select buildings.id, buildings.name, cities.name as city_name, regions.name as region_name, countries.name as country_name, buildings.address, buildings.nb_floors From buildings, cities, regions, countries where buildings.city_id=cities.id and cities.region_id=regions.id and regions.country_id=countries.id";
	public static final String FIND_REQUESTED_ROOM_DAILY_TEMPLATE = 
			"select * from %s where day >:fromDate and day <:toDate order by day";
	public static final String FIND_REQUESTED_MEETINGROOM_DAILY_TEMPLATE = 
			"select * from %s where day >:fromDate and day <:toDate order by day";
	public static final String FIND_LATEST_RESERVED_ROOM_TEMPLATE = 
			"select * from %s where user_id=:userId order by reservation_date desc";
	public static final String REMOVE_BY_MAC_ADDRESS_TEMPLATE = 
			"delete from %s where mac_address=:macAddress";
	public static final String REMOVE_BY_BUILDING_ID_TEMPLATE = 
			"delete from %s where building_id=:buildingId";
	public static final String REMOVE_BY_BUILDING_ID_AND_FLOOR_TEMPLATE = 
			"delete from %s where building_id=:buildingId and floor=:floor";
	public static final String REMOVE_BY_GATEWAY_ID_TEMPLATE = 
			"delete from %s where gateway_id=:gatewayId";
	public static final String REMOVE_BY_SENSOR_ID_TEMPLATE = 
			"delete from %s where sensor_id=:sensorId";
	public static final String REMOVE_BY_IDENTIFIER_TEMPLATE = 
			"delete from %s where identifier=:identifier";
	public static final String REMOVE_BY_DAY_ROOM_DAILY_TEMPLATE = 
			"delete from %s where day<:day";
	public static final String REMOVE_BY_DAY_MEETINGROOM_DAILY_TEMPLATE = 
			"delete from %s where day<:day";
	public static final String REMOVE_BY_DATE_ROOM_STATS_TEMPLATE = 
			"delete from %s where begin_occupancy_date<:date";
	public static final String REMOVE_BY_DATE_MEETINGROOM_STATS_TEMPLATE = 
			"delete from %s where begin_occupancy_date<:date";
	public static final String REMOVE_STAT_BY_ROOM_ID_TEMPLATE = 
			"delete from %s where room_id =:roomId";
	public static final String REMOVE_STAT_BY_MEETINGROOM_ID_TEMPLATE = 
			"delete from %s where meetingroom_id =:meetingroomId";
	public static final String REMOVE_PREFERENCES_BY_COUNTRY_ID_TEMPLATE = 
			"delete from %s where country_id =:countryId";
	public static final String REMOVE_PREFERENCES_BY_REGION_ID_TEMPLATE = 
			"delete from %s where region_id =:regionId";
	public static final String REMOVE_PREFERENCES_BY_CITY_ID_TEMPLATE = 
			"delete from %s where city_id =:cityId";
	public static final String REMOVE_PREFERENCES_BY_BUILDING_ID_TEMPLATE = 
			"delete from %s where building_id =:buildingId";
	public static final String REMOVE_PREFERENCES_BY_USER_ID_TEMPLATE = 
			"delete from %s where user_id =:userId";
	public static final String REMOVE_TEMPLATE = 
			"delete from %s where id=:id";
	public static final String REMOVE_ALL_TEMPLATE = 
			"delete from %s";
	public static final String COUNT_TEMPLATE =
			"select count(id) from %s";
	public static final String COUNT_SENSORS_BY_TYPE_AND_ROOM_ID_TEMPLATE =
			"select count(id) from %s where type=CAST(:type AS sensortype) and room_id=:roomId";
	public static final String COUNT_ACTIVE_USERS_TEMPLATE =
			"select count(id) from %s where last_connection_date >:lastConnexionDate";
	public static final String COUNT_ROOM_BY_TYPE_TEMPLATE =
			"select count(id) from %s where type=CAST(:type AS roomtype)";
	public static final String COUNT_MEETINGROOM_BY_TYPE_TEMPLATE =
			"select count(id) from %s where type=CAST(:type AS meetingroomtype)";
	public static final String CREATE_ALERT_TEMPLATE = 
			"insert into %s (name, type, gateway_id, sensor_id) values (:name, CAST(:type AS deviceType), :gatewayId, :sensorId)";
	public static final String CREATE_USER_TEMPLATE = 
			"insert into %s (first_name, last_name, email) values (:firstName, :lastName, :email)";
	public static final String CREATE_USER_FROM_USERUI_TEMPLATE = 
			"insert into %s (first_name, last_name, email, is_created_from_userui, access_token, expired_token_date) values (:firstName, :lastName, :email, :isCreatedFromUserui, :accessToken, :expiredTokenDate)";
	public static final String CREATE_GATEWAY_TEMPLATE = 
			"insert into %s (mac_address, name, description) values (:macAddress, :name, :description)";
	public static final String CREATE_MEETINGROOM_GROUPS_CONFIGURATION_TEMPLATE = 
			"insert into %s (building_id, floor, meetingroom_group_id) values (:buildingId, :floor, :meetingroomGroupId)";
	public static final String CREATE_AGENT_TEMPLATE = 
			"insert into %s (mac_address, name, description) values (:macAddress, :name, :description)";
	public static final String CREATE_DASHBOARD_TEMPLATE = 
			"insert into %s (mac_address, name, description, city_id, building_id, floor) values (:macAddress, :name, :description, :cityId, :buildingId, :floor)";
	public static final String CREATE_ROOM_TEMPLATE = 
			"insert into %s (name, gateway_id, capacity, description, type, status, building_id, floor) values (:name, :gatewayId, :capacity, :description, CAST(:type AS roomtype), CAST(:status AS roomstatus), :buildingId, :floor)";
	public static final String CREATE_MEETINGROOM_TEMPLATE = 
			"insert into %s (name, agent_id, capacity, description, type, status, external_id, building_id, floor) values (:name, :agentId, :capacity, :description, CAST(:type AS meetingroomtype), CAST(:status AS meetingroomstatus), :externalId, :buildingId, :floor)";
	public static final String CREATE_COUNTRY_TEMPLATE = 
			"insert into %s (name) values (:name)";
	public static final String CREATE_REGION_TEMPLATE = 
			"insert into %s (name, country_id) values (:name, :countryId)";
	public static final String CREATE_CITY_TEMPLATE = 
			"insert into %s (name, region_id) values (:name, :regionId)";
	public static final String CREATE_BUILDING_TEMPLATE = 
			"insert into %s (name, address, nb_floors, city_id) values (:name, :address, :nbFloors, :cityId)";
	public static final String CREATE_PREFERENCE_TEMPLATE = 
			"insert into %s (user_id, country_id, region_id, city_id, building_id, floor) values (:userId, :countryId, :regionId, :cityId, :buildingId, :floor)";
	public static final String CREATE_TEACHIN_SENSOR_TEMPLATE = 
			"insert into %s (sensor_identifier, sensor_status) values (:sensorIdentifier, CAST(:sensorStatus AS sensorTeachinStatus))";
	public static final String CREATE_TEACHIN_STATUS_TEMPLATE = 
			"insert into %s (room_id, gateway_id, teachin_status, user_id) values (:roomId, :gatewayId, CAST(:teachinStatus AS teachinStatus), :userId)";
	public static final String CREATE_RESERVED_ROOMSTAT_TEMPLATE = 
			"insert into %s (room_id, user_id, reservation_date, room_info) values (:roomId, :userId, now(), CAST(:roomInfo AS roomInfo))";
	public static final String CREATE_ROOMDAILY_TEMPLATE = 
			"insert into %s (room_id, occupancy_duration) values (:roomId, :occupancyDuration)";
	public static final String CREATE_MEETINGROOM_DAILY_TEMPLATE = 
			"insert into %s (meetingroom_id, occupancy_duration) values (:meetingroomId, :occupancyDuration)";
	public static final String CREATE_OCCUPIED_ROOMSTAT_TEMPLATE = 
			"insert into %s (room_id, room_info, begin_occupancy_date) values (:roomId, CAST(:roomInfo AS roomInfo), now())";
	public static final String CREATE_OCCUPIED_MEETINGROOMSTAT_TEMPLATE = 
			"insert into %s (meetingroom_id, meetingroom_info, begin_occupancy_date) values (:meetingRoomId, CAST(:meetingRoomInfo AS meetingRoomInfo), :beginOccupancyDate)";
	public static final String CREATE_SENSOR_TEMPLATE = 
			"insert into %s (identifier, name, type, profile, description, status, room_id) values (:identifier, :name, CAST(:type AS sensortype), :profile, :description, CAST(:status AS sensorstatus), :roomId)";
	public static final String UPDATE_RESERVED_ROOMSTAT_TEMPLATE = 
			"update %s set room_info=CAST(:roomInfo AS roomInfo) WHERE room_id=:roomId and user_id=:userId and room_info='RESERVED'";
	public static final String UPDATE_ROOMSTAT_BY_ID_TEMPLATE = 
			"update %s set room_info=CAST(:roomInfo AS roomInfo) WHERE id=:id";
	public static final String UPDATE_MEETINGROOMSTAT_BY_ID_TEMPLATE = 
			"update %s set meetingroom_info=CAST(:meetingRoomInfo AS meetingRoomInfo) WHERE id=:id";
	public static final String UPDATE_OCCUPIED_ROOMSTAT_TEMPLATE = 
			"update %s set room_info='OCCUPIED', begin_occupancy_date=now(), is_reservation_honored=true WHERE room_id=:roomId and room_info='RESERVED'";
	public static final String UPDATE_UNOCCUPIED_ROOMSTAT_TEMPLATE = 
			"update %s set room_info='UNOCCUPIED', end_occupancy_date=now() WHERE room_id=:roomId and room_info='OCCUPIED'";
	public static final String UPDATE_UNOCCUPIED_MEETINGROOMSTAT_TEMPLATE = 
			"update %s set meetingroom_info='UNOCCUPIED', end_occupancy_date=now() WHERE meetingroom_id=:meetingRoomId and meetingroom_info='OCCUPIED'";
	public static final String UPDATE_GATEWAY_TEMPLATE = 
			"update %s set name=:name, description=:description WHERE mac_address=:macAddress";
	public static final String UPDATE_AGENT_TEMPLATE = 
			"update %s set name=:name, description=:description WHERE mac_address=:macAddress";
	public static final String UPDATE_MEETINGROOM_GROUPS_CONFIGURATION_TEMPLATE = 
			"update %s set meetingroom_group_id=:meetingroomGroupId WHERE building_id=:buildingId and floor=:floor";
	public static final String UPDATE_DASHBOARD_TEMPLATE = 
			"update %s set name=:name, description=:description, city_id=:cityId, building_id=:buildingId, floor=:floor WHERE mac_address=:macAddress";
	public static final String UPDATE_USER_TEMPLATE =
			"update %s set first_name=:firstName, last_name=:lastName, email=:email WHERE id=:id";
	public static final String UPDATE_USER_ACCESS_TOKEN_TEMPLATE =
			"update %s set access_token=NULL  WHERE access_token=:accessToken";
	public static final String UPDATE_USER_BY_MAIL_TEMPLATE =
			"update %s set access_token=:accessToken, expired_token_date=:expiredTokenDate, first_name=:firstName, last_name=:lastName, last_connection_date=now() WHERE email=:email";
	public static final String UPDATE_GATEWAY_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS gatewayStatus), last_polling_date=now() where id=:id";
	public static final String UPDATE_AGENT_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS agentStatus), last_measure_date=now() where id=:id";
	public static final String UPDATE_AGENT_STATUS_FOR_TIMEOUT_TEMPLATE =
			"update %s set status=CAST(:status AS agentStatus) where id=:id";
	public static final String UPDATE_AGENT_MEETINGROOM_ID_TEMPLATE =
			"update %s set meetingroom_id=:meetingroomId where id=:id";
	public static final String UPDATE_DASHBOARD_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS dashboardStatus), last_measure_date=now() where id=:id";
	public static final String UPDATE_DASHBOARD_STATUS_FOR_TIMEOUT_TEMPLATE =
			"update %s set status=CAST(:status AS dashboardStatus) where id=:id";
	public static final String UPDATE_GATEWAY_COMMAND_TEMPLATE =
			"update %s set command=CAST(:command AS commandModel) where id=:id";
	public static final String UPDATE_ROOM_TEMPLATE =
			"update %s set name=:name, gateway_id=:gatewayId, building_id=:buildingId, floor=:floor, capacity=:capacity, description=:description, type=CAST(:type AS roomtype) WHERE id=:id";
	public static final String UPDATE_MEETINGROOM_TEMPLATE =
			"update %s set name=:name, agent_id=:agentId, external_id=:externalId, building_id=:buildingId, floor=:floor, capacity=:capacity, description=:description, type=CAST(:type AS meetingroomtype) WHERE id=:id";
	public static final String UPDATE_COUNTRY_TEMPLATE =
			"update %s set name=:name WHERE id=:id";
	public static final String UPDATE_REGION_TEMPLATE =
			"update %s set name=:name, country_id=:countryId WHERE id=:id";
	public static final String UPDATE_CITY_TEMPLATE =
			"update %s set name=:name, region_id=:regionId WHERE id=:id";
	public static final String UPDATE_BUILDING_TEMPLATE =
			"update %s set name=:name, city_id=:cityId, address=:address, nb_floors=:nbFloors WHERE id=:id";
	public static final String UPDATE_PREFERENCE_TEMPLATE =
			"update %s set country_id=:countryId, region_id=:regionId, city_id=:cityId, building_id=:buildingId, floor=:floor WHERE id=:id";
	public static final String UPDATE_TEACHIN_STATUS_TEMPLATE =
			"update %s set teachin_status=CAST(:teachinStatus AS teachinStatus) WHERE id=:id";
	public static final String UPDATE_TEACHIN_DATE_TEMPLATE =
			"update %s set teachin_date=now() WHERE id=:id";
	public static final String UPDATE_ROOM_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS roomstatus), temperature=:temperature, humidity=:humidity, last_measure_date=now(), user_id=:userId where id=:id";
	public static final String UPDATE_MEETINGROOM_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS meetingroomstatus), last_measure_date=now(), organizerlabel=:organizerLabel where id=:id";
	public static final String UPDATE_MEETINGROOM_DATA_TEMPLATE =
			"update %s set status=CAST(:status AS meetingroomstatus), last_measure_date=now(), organizerlabel=:organizerLabel, start_date=:startDate, end_date=:endDate where id=:id";
	public static final String UPDATE_ALERT_TEMPLATE =
			"update %s set name=:name, last_notification=:lastNotification where id=:id";
	public static final String UPDATE_SENSOR_TEMPLATE =
			"update %s set name=:name, type=CAST(:type AS sensortype), profile=:profile, description=:description, room_id=:roomId WHERE identifier=:identifier";
	public static final String UPDATE_SENSOR_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS sensorstatus), occupancy_info=CAST(:occupancyInfo AS sensorOccupancyInfo), last_measure_date=now() WHERE identifier=:identifier";
	public static final String UPDATE_SENSOR_ROOM_ID_TEMPLATE =
			"update %s set room_id=:roomId WHERE identifier=:identifier";

	private DataSqlTemplate() {
		
	}
}
