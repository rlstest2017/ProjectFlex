ALTER TABLE ONLY regions
	ADD CONSTRAINT regions_unique_key UNIQUE (name, country_id);

ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_unique_key UNIQUE (name, region_id);

ALTER TABLE ONLY buildings
    ADD CONSTRAINT buildings_unique_key UNIQUE (name, city_id);
   