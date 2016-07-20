'We can"t delete user in users Table, if his id is present in rooms Table'
ALTER TABLE rooms
	ADD  FOREIGN KEY(user_id) REFERENCES users(id);

'We can"t delete gateway in gateways Table, if his id is present in rooms Table'
ALTER TABLE rooms
	ADD  FOREIGN KEY(gateway_id) REFERENCES gateways(id);
