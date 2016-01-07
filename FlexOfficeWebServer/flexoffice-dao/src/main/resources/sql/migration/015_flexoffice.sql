
ALTER TABLE rooms
	ADD COLUMN last_measure_date timestamp without time zone DEFAULT now() NOT NULL;

