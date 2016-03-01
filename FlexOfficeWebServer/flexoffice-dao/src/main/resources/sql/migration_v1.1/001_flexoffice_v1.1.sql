
CREATE TABLE countries (
    id serial NOT NULL,
    name character varying(100) unique NOT NULL
);

CREATE TABLE regions (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    country_id integer
);

CREATE TABLE cities (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    region_id integer
);

CREATE TABLE buildings (
    id serial NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(255),
    city_id integer,
    nb_floors integer
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

ALTER TABLE ONLY countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (id);

ALTER TABLE ONLY regions
    ADD CONSTRAINT regions_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(country_id) REFERENCES countries(id);

ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(region_id) REFERENCES regions(id);

ALTER TABLE ONLY buildings
    ADD CONSTRAINT buildings_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(city_id) REFERENCES cities(id);

ALTER TABLE ONLY preferences
    ADD CONSTRAINT preferences_pkey PRIMARY KEY (id),
    ADD  FOREIGN KEY(country_id) REFERENCES countries(id),
    ADD  FOREIGN KEY(region_id) REFERENCES regions(id),
    ADD  FOREIGN KEY(city_id) REFERENCES cities(id),
    ADD  FOREIGN KEY(building_id) REFERENCES buildings(id),
    ADD  FOREIGN KEY(user_id) REFERENCES users(id);

ALTER TABLE rooms
	ADD COLUMN building_id integer,
	ADD COLUMN floor integer,
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id),
	DROP CONSTRAINT rooms_name_key;




