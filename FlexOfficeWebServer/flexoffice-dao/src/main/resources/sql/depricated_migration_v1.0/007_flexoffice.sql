
CREATE TABLE configuration (
    id serial NOT NULL,
    "key" character varying(50) unique NOT NULL,
    "value" character varying(20) NOT NULL,
    description text
);

ALTER TABLE ONLY configuration
    ADD CONSTRAINT configuration_pkey PRIMARY KEY (id);

    
INSERT INTO configuration ("key", "value", description) VALUES
    ('LAST_CONNECTION_DURATION', '15', 'This data is in days'),
    ('OCCUPANCY_TIMEOUT', '3', 'This data is in minutes'),
    ('BOOKING_DURATION', '300', 'This data is in secondes'),
    ('LAST_RESERVED_COUNT', '0', 'This data is number of rooms to return, 0 means all rooms'),
    ('DATE_BEGIN_DAY', '07:30', 'This data is in format hour:minutes'),
    ('DATE_END_DAY', '20:00', 'This data is in format hour:minutes');