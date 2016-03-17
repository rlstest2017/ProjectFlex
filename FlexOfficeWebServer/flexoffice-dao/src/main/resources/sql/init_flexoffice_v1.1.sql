
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

INSERT INTO countries (name) VALUES ('Country 1');
INSERT INTO regions (name, country_id) VALUES ('Region 1', 1);
INSERT INTO cities (name, region_id) VALUES ('City 1', 1);
INSERT INTO buildings (name, city_id, nb_floors) VALUES ('Building 1', 1, 10); 

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

ALTER TABLE rooms
	ADD COLUMN building_id integer NOT NULL DEFAULT 1,
	ADD COLUMN floor integer NOT NULL DEFAULT 1,
	DROP COLUMN address,
	ADD  FOREIGN KEY(building_id) REFERENCES buildings(id),
	DROP CONSTRAINT rooms_name_key,
	ADD CONSTRAINT rooms_unique_key UNIQUE (name, building_id);

INSERT INTO configuration ("key", "value", description) VALUES
    ('THRESHOLD_ENABLED_ADVANCEDRESEARCH_OF_ROOMS', '20', 'more than this parameter value, the advanced research of rooms is activated');



