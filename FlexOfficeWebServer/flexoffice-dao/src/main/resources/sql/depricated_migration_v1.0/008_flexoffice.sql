
ALTER TABLE room_stats
	ADD COLUMN user_id integer,
	ADD COLUMN reservation_date timestamp without time zone,
	ADD COLUMN is_reservation_honored boolean DEFAULT false,
	ADD COLUMN is_reservation_timeout boolean DEFAULT false,
	DROP COLUMN room_type;
	
