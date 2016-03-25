CREATE TYPE teachinStatus AS ENUM ('INITIALIZING', 'RUNNING', 'ENDED');
CREATE TYPE sensorTeachinStatus AS ENUM ('NOT_PAIRED', 'PAIRED_OK', 'PAIRED_KO');

CREATE TABLE teachin_sensors (
    id serial NOT NULL,
    room_id integer,
    gateway_id integer,
    sensor_identifier character varying(100),
    sensor_status sensorTeachinStatus,
    teachin_status teachinStatus
);

ALTER TABLE ONLY teachin_sensors
    ADD CONSTRAINT teachin_sensors_pkey PRIMARY KEY (id);


