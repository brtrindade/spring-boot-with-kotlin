CREATE TABLE public.person (
    id serial primary key,
    address character varying(100) NOT NULL,
    first_name character varying(80) NOT NULL,
    gender character varying(255) NOT NULL,
    last_name character varying(80) NOT NULL
);
