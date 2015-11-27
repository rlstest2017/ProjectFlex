
ALTER TABLE rooms
	ADD  FOREIGN KEY(user_id) REFERENCES users(id);
	
ALTER TABLE rooms
	ADD  FOREIGN KEY(gateway_id) REFERENCES gateways(id);