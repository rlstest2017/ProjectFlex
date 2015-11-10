CREATE TYPE userRole AS ENUM ('DEFAULT', 'ADMIN');
CREATE TYPE deviceType AS ENUM ('GATEWAY', 'SENSOR');
CREATE TYPE sensorType AS ENUM ('MOTION_DETECTION', 'TEMPERATURE_HUMIDITY');
CREATE TYPE sensorStatus AS ENUM ('ONLINE', 'OFFLINE', 'UNSTABLE');
CREATE TYPE gatewayStatus AS ENUM ('ONLINE', 'OFFLINE', 'ONTEACHIN');
CREATE TYPE roomStatus AS ENUM ('FREE', 'RESERVED', 'OCCUPIED', 'UNKNOWN');
CREATE TYPE roomType AS ENUM ('BOX', 'VIDEO_CONF');

CREATE TABLE users (
    id serial NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    email character varying(100),
    password character varying(100),
    access_token character varying(255),
    role userRole,
    last_connection_date timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE gateways (
    id serial NOT NULL,
    name character varying(100),
    mac_address character varying(100),
    description text,
    status gatewayStatus,
    is_activated boolean,
    rooms_id integer ARRAY,
    last_polling_date timestamp without time zone 
);

CREATE TABLE rooms (
    id serial NOT NULL,
    name character varying(100),
    address character varying(255),
    capacity integer,
    description text,
    status roomStatus,
    "type" roomType,
    gateway_id integer NOT NULL,
    user_id integer,
    sensors_id integer ARRAY
);

CREATE TABLE sensors (
    id serial NOT NULL,
    identifier character varying(100),
    name character varying(100),
    "type" sensorType,
    profile character varying(255),
    description text,
    status sensorStatus,
    room_id integer,
    last_measure_date timestamp without time zone 
);

CREATE TABLE alerts (
    id serial NOT NULL,
    name character varying(100),
    "type" deviceType,
    last_notification timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE room_stats (
    id serial NOT NULL,
    room_id integer NOT NULL,
    room_type roomType,
    begin_occupancy_date timestamp without time zone,
    end_occupancy_date timestamp without time zone
);


ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY gateways
    ADD CONSTRAINT gateways_pkey PRIMARY KEY (id);

ALTER TABLE ONLY rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sensors
    ADD CONSTRAINT sensors_pkey PRIMARY KEY (id);

ALTER TABLE ONLY alerts
    ADD CONSTRAINT alerts_pkey PRIMARY KEY (id);

ALTER TABLE ONLY room_stats
    ADD CONSTRAINT room_stats_pkey PRIMARY KEY (id);
 
CREATE INDEX users_last_name_idx ON users USING btree (last_name);

CREATE INDEX gateways_mac_address_idx ON gateways USING btree (mac_address);

CREATE INDEX sensors_identifier_idx ON sensors USING btree (identifier);

CREATE INDEX rooms_gateway_id_idx ON rooms USING btree (gateway_id);

CREATE INDEX room_stats_room_id_idx ON room_stats USING btree (room_id);
CREATE INDEX room_stats_room_type_idx ON room_stats USING btree (room_type);

