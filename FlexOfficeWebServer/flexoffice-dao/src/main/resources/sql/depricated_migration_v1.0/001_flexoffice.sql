
ALTER TABLE users ADD COLUMN expired_token_date timestamp without time zone;
ALTER TABLE users ADD COLUMN is_created_from_userui boolean DEFAULT false;

/* ALTER TABLE users DROP COLUMN expired_token_date;
ALTER TABLE users DROP COLUMN is_created_from_userui; */

