DROP TABLE meetingrooms, agents, dashboards, meetingroom_groups_configuration, meetingroom_stats, meetingroom_daily_occupancy;

CREATE EXTENSION IF NOT EXISTS citext;

/* Start Migration part */
alter table buildings alter column name type citext;
alter table buildings alter column name SET NOT NULL;
alter table buildings 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);

alter table cities alter column name type citext;
alter table cities alter column name SET NOT NULL;
alter table cities 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);

alter table regions alter column name type citext;
alter table regions alter column name SET NOT NULL;
alter table regions 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);

alter table countries alter column name type citext;
alter table countries alter column name SET NOT NULL;
alter table countries 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);
	
alter table gateways alter column name type citext;
alter table gateways alter column name SET NOT NULL;
alter table gateways 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);
	
alter table rooms alter column name type citext;
alter table rooms alter column name SET NOT NULL;
alter table rooms 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);
	
alter table sensors alter column name type citext;
alter table sensors 
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);
	
/*
 * !!!!!!! WARNING these 5 following lines have to be executed one by one !!!!!!!!!!!!
 * 
 */	
alter type deviceType add value 'AGENT';
alter type deviceType add value 'DASHBOARD';

alter type commandModel add value 'ONLINE';
alter type commandModel add value 'OFFLINE';
alter type commandModel add value 'NONE';
/* End Migration part */

CREATE TYPE meetingroomStatus AS ENUM ('FREE', 'OCCUPIED', 'UNKNOWN', 'ACK');
CREATE TYPE meetingroomType AS ENUM ('BOX', 'VIDEO_CONF');
CREATE TYPE agentStatus AS ENUM ('ONLINE', 'OFFLINE', 'ECONOMIC', 'STANDBY');
CREATE TYPE dashboardStatus AS ENUM ('ONLINE', 'OFFLINE', 'ECONOMIC', 'STANDBY');
CREATE TYPE meetingroomInfo AS ENUM ('TIMEOUT', 'OCCUPIED', 'UNOCCUPIED');

CREATE TABLE meetingrooms (
    id serial NOT NULL,
    external_id character varying(100) unique NOT NULL,
    name citext NOT NULL,
    organizerLabel character varying(300),
    capacity integer,
    description text,
    status meetingroomStatus DEFAULT 'UNKNOWN',
    "type" meetingroomType DEFAULT 'BOX',
    agent_id integer NOT NULL,
    building_id integer NOT NULL DEFAULT 1,
    floor integer NOT NULL DEFAULT 1,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    last_measure_date timestamp without time zone 
);

CREATE TABLE agents (
    id serial NOT NULL,
    mac_address character varying(100) unique NOT NULL,
    name citext unique NOT NULL,
    description text,
    status agentStatus DEFAULT 'OFFLINE',
    meetingroom_id integer DEFAULT 0,
    last_measure_date timestamp without time zone,
    command commandModel
);

CREATE TABLE dashboards (
    id serial NOT NULL,
    mac_address character varying(100) unique NOT NULL,
    name citext unique NOT NULL,
    description text,
    status dashboardStatus DEFAULT 'OFFLINE',
    last_measure_date timestamp without time zone,
    city_id integer NOT NULL,
    building_id integer,
    floor integer,
    command commandModel
);

CREATE TABLE meetingroom_groups_configuration (
    id serial NOT NULL,
    building_id integer NOT NULL,
    floor integer NOT NULL,
    meetingroom_group_id character varying(100) NOT NULL
);

CREATE TABLE meetingroom_stats (
    id serial NOT NULL,
    meetingroom_id integer NOT NULL,
    begin_occupancy_date timestamp without time zone,
    end_occupancy_date timestamp without time zone,
    meetingroom_info meetingroomInfo
);

CREATE TABLE meetingroom_daily_occupancy (
    id serial NOT NULL,
    meetingroom_id integer NOT NULL,
    day timestamp without time zone DEFAULT now() NOT NULL,
    occupancy_duration bigint
);	

ALTER TABLE ONLY agents
    ADD CONSTRAINT agents_pkey PRIMARY KEY (id),
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);

ALTER TABLE ONLY dashboards
    ADD CONSTRAINT dashboards_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(city_id) REFERENCES cities(id),
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id),
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100);

ALTER TABLE ONLY meetingrooms
    ADD CONSTRAINT meetingrooms_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(agent_id) REFERENCES agents(id),
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id),
	ADD CONSTRAINT meetingrooms_unique_key UNIQUE (name, building_id),
	ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 100),
	ADD CONSTRAINT meetingrooms_agent_id_key UNIQUE (agent_id);

ALTER TABLE ONLY meetingroom_groups_configuration
    ADD CONSTRAINT meetingroom_groups_configuration_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(building_id) REFERENCES buildings(id);

ALTER TABLE ONLY meetingroom_stats
    ADD CONSTRAINT meetingroom_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY meetingroom_daily_occupancy
    ADD CONSTRAINT meetingroom_daily_occupancy_pkey PRIMARY KEY (id);
    
ALTER TABLE ONLY alerts
	ADD agent_id integer,
	ADD FOREIGN KEY(agent_id) REFERENCES agents(id),
	ADD dashboard_id integer,
	ADD FOREIGN KEY(dashboard_id) REFERENCES dashboards(id);

CREATE INDEX agents_mac_address_idx ON agents USING btree (mac_address);
CREATE INDEX agents_name_idx ON agents USING btree (name);

CREATE INDEX dashboards_mac_address_idx ON dashboards USING btree (mac_address);
CREATE INDEX dashboards_name_idx ON dashboards USING btree (name);

CREATE INDEX meetingrooms_name_idx ON meetingrooms USING btree (name);
CREATE INDEX meetingrooms_external_id_idx ON meetingrooms USING btree (external_id);

CREATE INDEX meetingroom_stats_meetingroom_id_idx ON meetingroom_stats USING btree (meetingroom_id);

CREATE INDEX meetingroom_daily_occupancy_meetingroom_id_idx ON meetingroom_daily_occupancy USING btree (meetingroom_id);
CREATE INDEX meetingroom_daily_occupancy_day_idx ON meetingroom_daily_occupancy USING btree (day);
    
INSERT INTO configuration ("key", "value", description) VALUES
    ('AGENT_STATUS_TIMEOUT', '3', 'This data is in minutes'),
    ('DASHBOARD_STATUS_TIMEOUT', '3', 'This data is in minutes'),
    ('MEETINGROOM_STATUS_TIMEOUT', '3', 'This data is in minutes'),
    ('WS_REFRESH_INTERVAL', '10', 'This data is in secondes'),
    ('INACTIVITY_TIME', '0', 'This data is in secondes'),
    ('HOUR_START', '7', 'This data is in hours'),
    ('HOUR_END', '20', 'This data is in hours'),
    ('ORGANIZER_MANDATORY', 'false', 'This data is in boolean'),
    ('SUBJECT_MANDATORY', 'false', 'This data is in boolean'),
    ('ACK_TIME', '5', 'This data is in minutes'),
    ('USER_CAN_CANCEL', 'true', 'This data is in boolean'),
    ('CAN_SHOW_SUBJECT', 'true', 'This data is in boolean'),
    ('CAN_SHOW_ORGANIZER', 'true', 'This data is in boolean'),
    ('DURATION_STEP', '15', 'This data is in minutes'),
    ('MAX_DURATION', '720', 'This data is in minutes'),
    ('PAGES_SHIFT_INTERVAL', '2', 'This data is in secondes'),
    ('NB_ROOMS_PER_PAGE', '4', 'This data is an integer'),
    ('VIRTUAL_KEYBOARD', 'false', 'This data is in boolean'),
    ('KEYBOARD_LANG', 'french', 'This data is in string'),
    ('DASHBOARD_START_DATE', '0', 'The time in the current Day used by dashboards, This data is in seconds'),
    ('DASHBOARD_MAX_BOOKINGS', '2', 'The max bookings returned by dashboards, This data is an integer'),
    ('SYNCHRO_TIME', '30', 'The synchr interval with currentDate of server, This data isin seconds');
