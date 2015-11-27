
ALTER TABLE rooms
	ADD  FOREIGN KEY(user_id) REFERENCES users(id);