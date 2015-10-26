CREATE TABLE characteristic_desc_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE characteristic_item_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE characteristics (
    id serial NOT NULL,    
    item_id character varying(255),
    descriptor_id character varying(255),
    weight real,
    comment character varying(255),
    "timestamp" timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE log_item_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE log_user_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE logs (
    id serial NOT NULL,
    user_id character varying(255),
    item_id character varying(255),
    rating real,
    comment character varying(255),
    "timestamp" timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE preference_desc_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE preference_user_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE preferences (
    id serial NOT NULL,
    user_id character varying(255),
    descriptor_id character varying(255),
    comment character varying(255),
    rating real,
    "timestamp" timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE relationship_friend_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE relationship_user_stats (
    id serial NOT NULL,
    object_id character varying(255),
    average real,
    count integer,
    sum real
);

CREATE TABLE relationships (
    id serial NOT NULL,
    user_id character varying(255),
    friend_id character varying(255),
    rating real,
    comment character varying(255),
    "timestamp" timestamp without time zone DEFAULT now() NOT NULL
);

CREATE TABLE simi_item_collab (
	id serial NOT NULL,
	object_id character varying(255),
	similar_object_id character varying(255),
	"value" real
);

CREATE TABLE simi_item_thema (
	id serial NOT NULL,
	object_id character varying(255),
	similar_object_id character varying(255),
	"value" real
);

ALTER TABLE ONLY characteristic_desc_stats
    ADD CONSTRAINT characteristic_desc_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY characteristic_item_stats
    ADD CONSTRAINT characteristic_item_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY characteristics
    ADD CONSTRAINT characteristics_pkey PRIMARY KEY (id);

ALTER TABLE ONLY log_item_stats
    ADD CONSTRAINT log_item_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY log_user_stats
    ADD CONSTRAINT log_user_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY logs
    ADD CONSTRAINT logs_pkey PRIMARY KEY (id);

ALTER TABLE ONLY preference_desc_stats
    ADD CONSTRAINT preference_desc_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY preference_user_stats
    ADD CONSTRAINT preference_user_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY preferences
    ADD CONSTRAINT preferences_pkey PRIMARY KEY (id);

ALTER TABLE ONLY relationship_friend_stats
    ADD CONSTRAINT relationship_friend_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY relationship_user_stats
    ADD CONSTRAINT relationship_user_stats_pkey PRIMARY KEY (id);

ALTER TABLE ONLY relationships
    ADD CONSTRAINT relationships_pkey PRIMARY KEY (id);

ALTER TABLE ONLY simi_item_collab
	ADD CONSTRAINT simi_item_callab_pkey PRIMARY KEY (id);
	
	
CREATE INDEX logs_item_id_idx ON logs USING btree (item_id);
CREATE INDEX logs_user_id_idx ON logs USING btree (user_id);

CREATE INDEX charac_descriptor_id_idx ON characteristics USING btree (descriptor_id);
CREATE INDEX charac_item_id_idx ON characteristics USING btree (item_id);

CREATE INDEX pref_user_id_idx ON preferences USING btree (user_id);
CREATE INDEX pref_descriptor_id_idx ON preferences USING btree (descriptor_id);

CREATE INDEX rel_user_id_idx ON relationships USING btree (user_id);
CREATE INDEX rel_friend_id_idx ON relationships USING btree (friend_id);


CREATE INDEX cds_idx ON characteristic_desc_stats USING btree (object_id);
CREATE INDEX cis_idx ON characteristic_item_stats USING btree (object_id);

CREATE INDEX lis_idx ON log_item_stats USING btree (object_id);
CREATE INDEX lus_idx ON log_user_stats USING btree (object_id);

CREATE INDEX pds_idx ON preference_desc_stats USING btree (object_id);
CREATE INDEX pus_idx ON preference_user_stats USING btree (object_id);

CREATE INDEX rus_idx ON relationship_user_stats USING btree (object_id);
CREATE INDEX rfs_idx ON relationship_friend_stats USING btree (object_id);

