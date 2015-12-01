
ALTER TABLE alerts
	ADD COLUMN gateway_id integer;
ALTER TABLE alerts
	ADD COLUMN sensor_id integer;

ALTER TABLE alerts
	ADD  FOREIGN KEY(gateway_id) REFERENCES gateways(id);
ALTER TABLE alerts	
	ADD  FOREIGN KEY(sensor_id) REFERENCES sensors(id);
