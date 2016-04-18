DROP TABLE meetingrooms, agents, dashboards, meetingroom_groups_configuration, meetingroom_stats, meetingroom_daily_occupancy;
DROP TABLE users, gateways, rooms, sensors, alerts, room_stats, room_daily_occupancy, teachin_sensors, configuration, preferences, buildings, cities, regions, countries;
   
DROP TYPE meetingroomStatus, meetingroomType, agentStatus, dashboardStatus, meetingroomInfo; 
DROP TYPE userRole, deviceType, sensorType, sensorStatus, gatewayStatus, roomStatus, roomType, sensorOccupancyInfo, roomInfo, teachinStatus, sensorTeachinStatus, commandModel;     


CREATE TYPE userRole AS ENUM ('DEFAULT', 'ADMIN');
CREATE TYPE deviceType AS ENUM ('GATEWAY', 'SENSOR');
CREATE TYPE sensorType AS ENUM ('MOTION_DETECTION', 'TEMPERATURE_HUMIDITY');
CREATE TYPE sensorStatus AS ENUM ('ONLINE', 'OFFLINE', 'UNSTABLE', 'UNSTABLE_VOLTAGE', 'UNSTABLE_RSSI');
CREATE TYPE gatewayStatus AS ENUM ('ONLINE', 'OFFLINE', 'ONTEACHIN', 'ERROR_NO_USB_DEVICE', 'ERROR_FIFO_FILE');
CREATE TYPE roomStatus AS ENUM ('FREE', 'RESERVED', 'OCCUPIED', 'UNKNOWN');
CREATE TYPE roomType AS ENUM ('BOX', 'VIDEO_CONF');
CREATE TYPE sensorOccupancyInfo AS ENUM ('UNOCCUPIED', 'OCCUPIED');
CREATE TYPE roomInfo AS ENUM ('RESERVED', 'CANCELED', 'TIMEOUT', 'OCCUPIED', 'UNOCCUPIED');
CREATE TYPE teachinStatus AS ENUM ('INITIALIZING', 'RUNNING', 'ENDED');
CREATE TYPE sensorTeachinStatus AS ENUM ('NOT_PAIRED', 'PAIRED_OK', 'PAIRED_KO');
CREATE TYPE commandModel AS ENUM ('RESET', 'ECONOMIC', 'STANDBY');

 
CREATE TABLE users (
    id serial NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    email character varying(100) unique NOT NULL,
    password character varying(100),
    access_token character varying(255),
    role userRole DEFAULT 'DEFAULT',
    last_connection_date timestamp without time zone DEFAULT now() NOT NULL,
    expired_token_date timestamp without time zone,
    is_created_from_userui boolean DEFAULT false
);

CREATE TABLE gateways (
    id serial NOT NULL,
    name character varying(100) unique NOT NULL,
    mac_address character varying(100) unique NOT NULL,
    description text,
    status gatewayStatus DEFAULT 'OFFLINE',
    last_polling_date timestamp without time zone,
    command commandModel
);

CREATE TABLE rooms (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    capacity integer,
    temperature real,
    humidity real,
    description text,
    status roomStatus DEFAULT 'UNKNOWN',
    "type" roomType DEFAULT 'BOX',
    gateway_id integer NOT NULL,
    user_id integer,
    building_id integer NOT NULL DEFAULT 1,
    floor integer NOT NULL DEFAULT 1,
    last_measure_date timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE sensors (
    id serial NOT NULL,
    identifier character varying(100) unique NOT NULL,
    name character varying(100),
    "type" sensorType DEFAULT 'MOTION_DETECTION',
    profile character varying(255),
    description text,
    status sensorStatus DEFAULT 'OFFLINE',
    room_id integer DEFAULT 0,
    last_measure_date timestamp without time zone,
    occupancy_info sensorOccupancyInfo DEFAULT 'UNOCCUPIED'
);

CREATE TABLE alerts (
    id serial NOT NULL,
    name character varying(100),
    "type" deviceType,
    last_notification timestamp without time zone DEFAULT now() NOT NULL,
    gateway_id integer,
    sensor_id integer
);

CREATE TABLE room_stats (
    id serial NOT NULL,
    room_id integer NOT NULL,
    begin_occupancy_date timestamp without time zone,
    end_occupancy_date timestamp without time zone,
    user_id integer,
    reservation_date timestamp without time zone,
    is_reservation_honored boolean DEFAULT false,
    room_info roomInfo
);

CREATE TABLE room_daily_occupancy (
    id serial NOT NULL,
    room_id integer NOT NULL,
    day timestamp without time zone DEFAULT now() NOT NULL,
    occupancy_duration bigint
);	

CREATE TABLE teachin_sensors (
    id serial NOT NULL,
    room_id integer,
    gateway_id integer,
    sensor_identifier character varying(100),
    sensor_status sensorTeachinStatus,
    teachin_status teachinStatus,
    user_id integer,
    teachin_date timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE configuration (
    id serial NOT NULL,
    "key" character varying(50) unique NOT NULL,
    "value" character varying(20) NOT NULL,
    description text
);

CREATE TABLE countries (
    id serial NOT NULL,
    name character varying(100) unique NOT NULL
);

CREATE TABLE regions (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    country_id integer NOT NULL
);

CREATE TABLE cities (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    region_id integer NOT NULL
);

CREATE TABLE buildings (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(255),
    city_id integer NOT NULL,
    nb_floors integer NOT NULL
);

CREATE TABLE preferences (
    id serial NOT NULL,
    user_id integer NOT NULL,
    country_id integer,
    region_id integer,
    city_id integer,
    building_id integer,
    floor integer
);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY gateways
    ADD CONSTRAINT gateways_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sensors
    ADD CONSTRAINT sensors_pkey PRIMARY KEY (id);

ALTER TABLE ONLY alerts
    ADD CONSTRAINT alerts_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(gateway_id) REFERENCES gateways(id),
    ADD  FOREIGN KEY(sensor_id) REFERENCES sensors(id);

ALTER TABLE ONLY room_stats
    ADD CONSTRAINT room_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY configuration
    ADD CONSTRAINT configuration_pkey PRIMARY KEY (id);

ALTER TABLE ONLY room_daily_occupancy
    ADD CONSTRAINT room_daily_occupancy_pkey PRIMARY KEY (id);

ALTER TABLE ONLY teachin_sensors
    ADD CONSTRAINT teachin_sensors_pkey PRIMARY KEY (id);

ALTER TABLE ONLY countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (id);

ALTER TABLE ONLY regions
    ADD CONSTRAINT regions_pkey PRIMARY KEY (id),
    	ADD CONSTRAINT regions_unique_key UNIQUE (name, country_id),
    ADD  FOREIGN KEY(country_id) REFERENCES countries(id);

ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id),
    ADD CONSTRAINT cities_unique_key UNIQUE (name, region_id),
    ADD  FOREIGN KEY(region_id) REFERENCES regions(id);

ALTER TABLE ONLY buildings
    ADD CONSTRAINT buildings_pkey PRIMARY KEY (id),
    ADD CONSTRAINT buildings_unique_key UNIQUE (name, city_id),
    ADD  FOREIGN KEY(city_id) REFERENCES cities(id);

ALTER TABLE ONLY preferences
    ADD CONSTRAINT preferences_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(country_id) REFERENCES countries(id),
    ADD  FOREIGN KEY(region_id) REFERENCES regions(id),
    ADD  FOREIGN KEY(city_id) REFERENCES cities(id),
    ADD  FOREIGN KEY(building_id) REFERENCES buildings(id),
    ADD  FOREIGN KEY(user_id) REFERENCES users(id);

ALTER TABLE ONLY rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id),
	ADD CONSTRAINT rooms_user_id_key UNIQUE (user_id),
	ADD CONSTRAINT rooms_unique_key UNIQUE (name, building_id),
	ADD  FOREIGN KEY(user_id) REFERENCES users(id),
	ADD  FOREIGN KEY(gateway_id) REFERENCES gateways(id),
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id);
    
CREATE INDEX users_last_name_idx ON users USING btree (last_name);
CREATE INDEX users_email_idx ON users USING btree (email);

CREATE INDEX gateways_mac_address_idx ON gateways USING btree (mac_address);
CREATE INDEX gateways_name_idx ON gateways USING btree (name);

CREATE INDEX sensors_identifier_idx ON sensors USING btree (identifier);
CREATE INDEX sensors_profile_idx ON sensors USING btree (profile);

CREATE INDEX rooms_gateway_id_idx ON rooms USING btree (gateway_id);
CREATE INDEX rooms_name_idx ON rooms USING btree (name);

CREATE INDEX room_stats_room_id_idx ON room_stats USING btree (room_id);

CREATE INDEX room_daily_occupancy_room_id_idx ON room_daily_occupancy USING btree (room_id);
CREATE INDEX room_daily_occupancy_day_idx ON room_daily_occupancy USING btree (day);

INSERT INTO configuration ("key", "value", description) VALUES
    ('LAST_CONNECTION_DURATION', '15', 'This data is in days'),
    ('OCCUPANCY_TIMEOUT', '3', 'This data is in minutes'),
    ('BOOKING_DURATION', '300', 'This data is in secondes'),
    ('LAST_RESERVED_COUNT', '0', 'This data is number of rooms to return, 0 means all rooms'),
    ('DATE_BEGIN_DAY', '07:30', 'This data is in format hour:minutes'),
    ('DATE_END_DAY', '20:00', 'This data is in format hour:minutes'),
    ('TEACHIN_TIMEOUT', '15', 'This data is in minutes'),
    ('KEEP_STAT_DATA_IN_DAYS', '365', 'This data is in days'),
    ('THRESHOLD_ENABLED_ADVANCEDRESEARCH_OF_ROOMS', '20', 'more than this parameter value, the advanced research of rooms is activated');

INSERT INTO users (first_name, last_name, email, password, role) VALUES ('admin', 'admin', 'admin@oab.com', 'flexoffice', 'ADMIN');
INSERT INTO countries (name) VALUES ('Country 1');
INSERT INTO regions (name, country_id) VALUES ('Region 1', 1);
INSERT INTO cities (name, region_id) VALUES ('City 1', 1);
INSERT INTO buildings (name, city_id, nb_floors) VALUES ('Building 1', 1, 10); 
			