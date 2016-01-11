
ALTER TABLE teachin_sensors
	ADD COLUMN teachin_date timestamp without time zone DEFAULT now() NOT NULL;

