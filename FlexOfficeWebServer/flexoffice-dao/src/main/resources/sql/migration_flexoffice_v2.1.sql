DROP TABLE IF EXISTS extra_property_def, extra_property_enum_values, product_def, rooms_extra_property_values, meetingrooms_extra_property_values;   

-------- EXTENSION --------
CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;
COMMENT ON EXTENSION citext IS 'data type for case-insensitive character strings';

CREATE TABLE product_def (
    id serial NOT NULL,
    name character varying(50) NOT NULL,
    description text
);

CREATE TABLE extra_property_def (
    id serial NOT NULL,
    name citext unique NOT NULL,
    product_def_id integer NOT NULL,
    is_number boolean DEFAULT false,
    number_min_value real,
    number_max_value real,
    description text
);

-- values of enums defined in extra_property_def
CREATE TABLE extra_property_enum_values (
    id serial NOT NULL,
    enum_value citext unique NOT NULL,
    property_def_id integer NOT NULL
);

CREATE TABLE rooms_extra_property_values (
    id serial NOT NULL,
    property_def_id integer NOT NULL,
    room_id integer,
    enum_value_id integer,
    number_value real
);

CREATE TABLE meetingrooms_extra_property_values (
    id serial NOT NULL,
    property_def_id integer NOT NULL,
    meetingroom_id integer,
    enum_value_id integer,
    number_value real
);

ALTER TABLE ONLY product_def
    ADD CONSTRAINT product_def_pkey PRIMARY KEY (id);

ALTER TABLE ONLY extra_property_def
    ADD CONSTRAINT extra_property_def_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(product_def_id) REFERENCES product_def(id),
    ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 50);

ALTER TABLE ONLY extra_property_enum_values
    ADD CONSTRAINT extra_property_enum_values_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(property_def_id) REFERENCES extra_property_def(id),
    ADD CONSTRAINT enum_value_length_check CHECK (char_length(enum_value) <= 50);

ALTER TABLE ONLY rooms_extra_property_values
    ADD CONSTRAINT rooms_extra_property_values_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(property_def_id) REFERENCES extra_property_def(id),
	ADD FOREIGN KEY(room_id) REFERENCES rooms(id),
    ADD FOREIGN KEY(enum_value_id) REFERENCES extra_property_enum_values(id);
    
ALTER TABLE ONLY meetingrooms_extra_property_values
    ADD CONSTRAINT meetingrooms_extra_property_values_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(property_def_id) REFERENCES extra_property_def(id),
    ADD FOREIGN KEY(meetingroom_id) REFERENCES meetingrooms(id),
    ADD FOREIGN KEY(enum_value_id) REFERENCES extra_property_enum_values(id);

---------- INITIAZING ----------
INSERT INTO product_def (name, description) VALUES 
		('ALL', 'corresponding extra properties are associated to all products'),
		('ROOM', 'corresponding extra properties are associated to non bookable rooms'),
		('MEETINGROOM', 'corresponding extra properties are associated to bookable rooms');
    