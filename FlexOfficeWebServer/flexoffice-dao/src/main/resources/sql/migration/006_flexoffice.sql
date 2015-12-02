CREATE TYPE sensorOccupancyInfo AS ENUM ('UNOCCUPIED', 'OCCUPIED');
ALTER TABLE sensors
	ADD COLUMN occupancy_info sensorOccupancyInfo DEFAULT 'UNOCCUPIED';
