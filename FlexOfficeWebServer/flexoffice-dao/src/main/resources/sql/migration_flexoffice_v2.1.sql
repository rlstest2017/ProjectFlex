DROP TABLE IF EXISTS extra_propertie_enum_def, extra_propertie_number_def, extra_propertie_enum_values, product_def, rooms_extra_propertie_values, meetingrooms_extra_propertie_values;   

-------- EXTENSION --------
CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;
COMMENT ON EXTENSION citext IS 'data type for case-insensitive character strings';

CREATE TABLE product_def (
    id serial NOT NULL,
    name character varying(50) NOT NULL,
    description text
);

CREATE TABLE extra_propertie_enum_def (
    id serial NOT NULL,
    name citext unique NOT NULL,
    product_def_id integer NOT NULL,
    description text
);

CREATE TABLE extra_propertie_number_def (
    id serial NOT NULL,
    name citext unique NOT NULL,
    product_def_id integer NOT NULL,
    number_min_value real,
    number_max_value real,
    description text
);

-- values of enums defined in extra_propertie_enum_names
CREATE TABLE extra_propertie_enum_values (
    id serial NOT NULL,
    enum_value citext unique NOT NULL,
    enum_def_id integer NOT NULL
);

CREATE TABLE rooms_extra_propertie_values (
    id serial NOT NULL,
    enum_def_id integer,
    number_def_id integer,
    room_id integer,
    enum_value_id integer,
    number_value real
);

CREATE TABLE meetingrooms_extra_propertie_values (
    id serial NOT NULL,
    enum_def_id integer,
    number_def_id integer,
    meetingroom_id integer,
    enum_value_id integer,
    number_value real
);

ALTER TABLE ONLY product_def
    ADD CONSTRAINT product_def_pkey PRIMARY KEY (id);

ALTER TABLE ONLY extra_propertie_enum_def
    ADD CONSTRAINT extra_propertie_enum_def_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(product_def_id) REFERENCES product_def(id),
    ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 50);

ALTER TABLE ONLY extra_propertie_number_def
    ADD CONSTRAINT extra_propertie_number_def_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(product_def_id) REFERENCES product_def(id),
    ADD CONSTRAINT name_length_check CHECK (char_length(name) <= 50);

ALTER TABLE ONLY extra_propertie_enum_values
    ADD CONSTRAINT extra_propertie_enum_values_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(enum_def_id) REFERENCES extra_propertie_enum_def(id),
    ADD CONSTRAINT enum_value_length_check CHECK (char_length(enum_value) <= 50);

ALTER TABLE ONLY rooms_extra_propertie_values
    ADD CONSTRAINT rooms_extra_propertie_values_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(enum_def_id) REFERENCES extra_propertie_enum_def(id),
	ADD FOREIGN KEY(number_def_id) REFERENCES extra_propertie_number_def(id),
    ADD FOREIGN KEY(room_id) REFERENCES rooms(id),
    ADD FOREIGN KEY(enum_value_id) REFERENCES extra_propertie_enum_values(id);
    
ALTER TABLE ONLY meetingrooms_extra_propertie_values
    ADD CONSTRAINT meetingrooms_extra_propertie_values_pkey PRIMARY KEY (id),
    ADD FOREIGN KEY(enum_def_id) REFERENCES extra_propertie_enum_def(id),
	ADD FOREIGN KEY(number_def_id) REFERENCES extra_propertie_number_def(id),
    ADD FOREIGN KEY(meetingroom_id) REFERENCES meetingrooms(id),
    ADD FOREIGN KEY(enum_value_id) REFERENCES extra_propertie_enum_values(id);

---------- INITIAZING ----------
INSERT INTO product_def (name, description) VALUES 
		('ALL', 'corresponding extra properties are associated to all products'),
		('ROOM', 'corresponding extra properties are associated to non bookable rooms'),
		('MEETINGROOM', 'corresponding extra properties are associated to bookable rooms');
    