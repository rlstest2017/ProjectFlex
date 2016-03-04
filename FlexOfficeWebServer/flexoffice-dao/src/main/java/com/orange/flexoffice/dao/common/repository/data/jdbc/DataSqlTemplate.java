package com.orange.flexoffice.dao.common.repository.data.jdbc;

public final class DataSqlTemplate {
	
	public static final String FIND_ONE_TEMPLATE = 
			"select * from %s where id=:id";
	public static final String FIND_BY_COL_ID_TEMPLATE = 
			"select * from %s where %s=:columnId";
	public static final String FIND_REGION_DTO_BY_COL_ID_TEMPLATE = 
			"select regions.id, regions.name, countries.id as country_id, countries.name as country_name From regions, countries where regions.country_id=countries.id and regions.id=:columnId";
	public static final String FIND_BY_TEACHIN_STATUS_TEMPLATE = 
			"select * from %s where teachin_status is not null";
	public static final String FIND_BY_COL_KEY_TEMPLATE = 
			"select * from %s where key=:key";
	public static final String FIND_ROOMSTAT_BY_ROOMID_TEMPLATE = 
			"select * from %s where room_id=:roomId and room_info=CAST(:roomInfo AS roomInfo)";
	public static final String FIND_ROOMSTAT_BY_ROOMINFO_TEMPLATE = 
			"select * from %s where room_info=CAST(:roomInfo AS roomInfo)";
	public static final String FIND_BY_MAC_ADDRESS_TEMPLATE = 
			"select * from %s where mac_address=:macAddress";
	public static final String FIND_BY_COL_GATEWAY_ID_TEMPLATE = 
			"select * from %s where gateway_id=:gatewayId";
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
	public static final String FIND_BY_IDENTIFIER_TEMPLATE = 
			"select * from %s where identifier=:identifier";
	public static final String FIND_ALL_TEMPLATE = 
			"select * from %s order by %s";
	public static final String FIND_ALL_ROOM_DAILY_TEMPLATE = 
			"select * from %s order by room_id";
	public static final String FIND_REQUESTED_ROOM_DAILY_AND_TYPE_TEMPLATE = 
			"select room_daily_occupancy.day, room_daily_occupancy.occupancy_duration, rooms.type From room_daily_occupancy, rooms where rooms.id=room_daily_occupancy.room_id and room_daily_occupancy.day >:fromDate and room_daily_occupancy.day <:toDate order by room_daily_occupancy.day";
	public static final String FIND_ALL_UNOCCUPIED_DAILY_TEMPLATE = 
			"select * from %s where room_info=CAST(:roomInfo AS roomInfo) and begin_occupancy_date >:beginOccupancyDate and end_occupancy_date <:endOccupancyDate order by room_id";
	public static final String FIND_ALL_REGIONS_SUMMARY_TEMPLATE = 
	"select regions.id, regions.name, countries.name as country_name From regions, countries where regions.country_id=countries.id";
	public static final String FIND_REQUESTED_ROOM_DAILY_TEMPLATE = 
			"select * from %s where day >:fromDate and day <:toDate order by day";
	public static final String FIND_LATEST_RESERVED_ROOM_TEMPLATE = 
			"select * from %s where user_id=:userId order by reservation_date desc";
	public static final String REMOVE_BY_MAC_ADDRESS_TEMPLATE = 
			"delete from %s where mac_address=:macAddress";
	public static final String REMOVE_BY_GATEWAY_ID_TEMPLATE = 
			"delete from %s where gateway_id=:gatewayId";
	public static final String REMOVE_BY_SENSOR_ID_TEMPLATE = 
			"delete from %s where sensor_id=:sensorId";
	public static final String REMOVE_BY_IDENTIFIER_TEMPLATE = 
			"delete from %s where identifier=:identifier";
	public static final String REMOVE_BY_DAY_ROOM_DAILY_TEMPLATE = 
			"delete from %s where day<:day";
	public static final String REMOVE_BY_DATE_ROOM_STATS_TEMPLATE = 
			"delete from %s where begin_occupancy_date<:date";
	public static final String REMOVE_STAT_BY_ROOM_ID_TEMPLATE = 
			"delete from %s where room_id =:roomId";
	public static final String REMOVE_TEMPLATE = 
			"delete from %s where id=:id";
	public static final String REMOVE_ALL_TEMPLATE = 
			"delete from %s";
	public static final String COUNT_TEMPLATE =
			"select count(id) from %s";
	public static final String COUNT_ACTIVE_USERS_TEMPLATE =
			"select count(id) from %s where last_connection_date >:lastConnexionDate";
	public static final String COUNT_ROOM_BY_TYPE_TEMPLATE =
			"select count(id) from %s where type=CAST(:type AS roomtype)";
	public static final String CREATE_ALERT_TEMPLATE = 
			"insert into %s (name, type, gateway_id, sensor_id) values (:name, CAST(:type AS deviceType), :gatewayId, :sensorId)";
	public static final String CREATE_USER_TEMPLATE = 
			"insert into %s (first_name, last_name, email) values (:firstName, :lastName, :email)";
	public static final String CREATE_USER_FROM_USERUI_TEMPLATE = 
			"insert into %s (first_name, last_name, email, is_created_from_userui, access_token, expired_token_date) values (:firstName, :lastName, :email, :isCreatedFromUserui, :accessToken, :expiredTokenDate)";
	public static final String CREATE_GATEWAY_TEMPLATE = 
			"insert into %s (mac_address, name, description) values (:macAddress, :name, :description)";
	public static final String CREATE_ROOM_TEMPLATE = 
			"insert into %s (name, gateway_id, address, capacity, description, type, status, building_id) values (:name, :gatewayId, :address, :capacity, :description, CAST(:type AS roomtype), CAST(:status AS roomstatus), :buildingId)";
	public static final String CREATE_COUNTRY_TEMPLATE = 
			"insert into %s (name) values (:name)";
	public static final String CREATE_REGION_TEMPLATE = 
			"insert into %s (name, country_id) values (:name, :countryId)";
	public static final String CREATE_TEACHIN_SENSOR_TEMPLATE = 
			"insert into %s (sensor_identifier, sensor_status) values (:sensorIdentifier, CAST(:sensorStatus AS sensorTeachinStatus))";
	public static final String CREATE_TEACHIN_STATUS_TEMPLATE = 
			"insert into %s (room_id, gateway_id, teachin_status, user_id) values (:roomId, :gatewayId, CAST(:teachinStatus AS teachinStatus), :userId)";
	public static final String CREATE_RESERVED_ROOMSTAT_TEMPLATE = 
			"insert into %s (room_id, user_id, reservation_date, room_info) values (:roomId, :userId, now(), CAST(:roomInfo AS roomInfo))";
	public static final String CREATE_ROOMDAILY_TEMPLATE = 
			"insert into %s (room_id, occupancy_duration) values (:roomId, :occupancyDuration)";
	public static final String CREATE_OCCUPIED_ROOMSTAT_TEMPLATE = 
			"insert into %s (room_id, room_info, begin_occupancy_date) values (:roomId, CAST(:roomInfo AS roomInfo), now())";
	public static final String CREATE_SENSOR_TEMPLATE = 
			"insert into %s (identifier, name, type, profile, description, status, room_id) values (:identifier, :name, CAST(:type AS sensortype), :profile, :description, CAST(:status AS sensorstatus), :roomId)";
	public static final String UPDATE_RESERVED_ROOMSTAT_TEMPLATE = 
			"update %s set room_info=CAST(:roomInfo AS roomInfo) WHERE room_id=:roomId and user_id=:userId and room_info='RESERVED'";
	public static final String UPDATE_ROOMSTAT_BY_ID_TEMPLATE = 
			"update %s set room_info=CAST(:roomInfo AS roomInfo) WHERE id=:id";
	public static final String UPDATE_OCCUPIED_ROOMSTAT_TEMPLATE = 
			"update %s set room_info='OCCUPIED', begin_occupancy_date=now(), is_reservation_honored=true WHERE room_id=:roomId and room_info='RESERVED'";
	public static final String UPDATE_UNOCCUPIED_ROOMSTAT_TEMPLATE = 
			"update %s set room_info='UNOCCUPIED', end_occupancy_date=now() WHERE room_id=:roomId and room_info='OCCUPIED'";
	public static final String UPDATE_GATEWAY_TEMPLATE = 
			"update %s set name=:name, description=:description WHERE mac_address=:macAddress";
	public static final String UPDATE_USER_TEMPLATE =
			"update %s set first_name=:firstName, last_name=:lastName, email=:email WHERE id=:id";
	public static final String UPDATE_USER_ACCESS_TOKEN_TEMPLATE =
			"update %s set access_token=NULL  WHERE access_token=:accessToken";
	public static final String UPDATE_USER_BY_MAIL_TEMPLATE =
			"update %s set access_token=:accessToken, expired_token_date=:expiredTokenDate, first_name=:firstName, last_name=:lastName, last_connection_date=now() WHERE email=:email";
	public static final String UPDATE_GATEWAY_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS gatewayStatus), last_polling_date=now() where id=:id";
	public static final String UPDATE_GATEWAY_COMMAND_TEMPLATE =
			"update %s set command=CAST(:command AS commandModel) where id=:id";
	public static final String UPDATE_ROOM_TEMPLATE =
			"update %s set name=:name, gateway_id=:gatewayId, address=:address, capacity=:capacity, description=:description, type=CAST(:type AS roomtype) WHERE id=:id";
	public static final String UPDATE_COUNTRY_TEMPLATE =
			"update %s set name=:name WHERE id=:id";
	public static final String UPDATE_TEACHIN_STATUS_TEMPLATE =
			"update %s set teachin_status=CAST(:teachinStatus AS teachinStatus) WHERE id=:id";
	public static final String UPDATE_TEACHIN_DATE_TEMPLATE =
			"update %s set teachin_date=now() WHERE id=:id";
	public static final String UPDATE_ROOM_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS roomstatus), temperature=:temperature, humidity=:humidity, last_measure_date=now(), user_id=:userId where id=:id";
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
