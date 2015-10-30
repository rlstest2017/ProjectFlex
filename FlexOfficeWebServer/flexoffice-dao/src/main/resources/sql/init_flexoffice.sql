CREATE TABLE users_flexoffice (
    id serial NOT NULL,
    first_name character varying(30),
    last_name character varying(30),
    email character varying(30),
    password character varying(30),
    last_connection timestamp without time zone DEFAULT now() NOT NULL
);

ALTER TABLE ONLY users_flexoffice
    ADD CONSTRAINT users_flexoffice_pkey PRIMARY KEY (id);
