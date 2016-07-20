
ALTER TABLE room_daily_occupancy
	DROP COLUMN occupancy_duration,
	ADD COLUMN occupancy_duration bigint;

