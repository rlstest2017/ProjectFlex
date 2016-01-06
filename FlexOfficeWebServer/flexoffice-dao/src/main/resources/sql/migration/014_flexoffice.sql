CREATE TYPE commandModel AS ENUM ('RESET');
ALTER TABLE gateways
	ADD COLUMN command commandModel;

