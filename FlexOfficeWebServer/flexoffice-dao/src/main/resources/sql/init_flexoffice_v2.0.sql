CREATE TYPE meetingroomStatus AS ENUM ('FREE', 'OCCUPIED', 'UNKNOWN');
CREATE TYPE meetingroomType AS ENUM ('BOX', 'VIDEO_CONF');
CREATE TYPE agentStatus AS ENUM ('ONLINE', 'OFFLINE', 'ECONOMIC', 'STANDBY');
CREATE TYPE dashboardStatus AS ENUM ('ONLINE', 'OFFLINE', 'ECONOMIC', 'STANDBY');
CREATE TYPE meetingroomInfo AS ENUM ('TIMEOUT', 'OCCUPIED', 'UNOCCUPIED');

CREATE TABLE meetingrooms (
    id serial NOT NULL,
    external_id character varying(100) unique NOT NULL,
    name character varying(100) NOT NULL,
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
    name character varying(100),
    description text,
    status agentStatus DEFAULT 'OFFLINE',
    meetingroom_id integer DEFAULT 0,
    last_measure_date timestamp without time zone,
    command commandModel
);

CREATE TABLE dashboards (
    id serial NOT NULL,
    mac_address character varying(100) unique NOT NULL,
    name character varying(100),
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
    ADD CONSTRAINT agents_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dashboards
    ADD CONSTRAINT dashboards_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(city_id) REFERENCES cities(id),
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id);

ALTER TABLE ONLY meetingrooms
    ADD CONSTRAINT meetingrooms_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(agent_id) REFERENCES agents(id),
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id);

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
    ('MEETINGROOM_STATUS_TIMEOUT', '3', 'This data is in minutes');

