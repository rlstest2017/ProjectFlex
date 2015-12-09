
CREATE TABLE room_daily_occupancy (
    id serial NOT NULL,
    room_id integer NOT NULL,
    occupancy_duration real,
    day timestamp without time zone DEFAULT now() NOT NULL
);	
	
ALTER TABLE ONLY room_daily_occupancy
    ADD CONSTRAINT room_daily_occupancy_pkey PRIMARY KEY (id);
