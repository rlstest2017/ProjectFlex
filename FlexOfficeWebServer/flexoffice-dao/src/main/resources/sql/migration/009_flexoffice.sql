CREATE TYPE roomInfo AS ENUM ('RESERVED', 'CANCELED', 'TIMEOUT', 'OCCUPIED', 'UNOCCUPIED');

ALTER TABLE room_stats
	ADD COLUMN room_info roomInfo,
	DROP COLUMN is_reservation_timeout;
	
	
