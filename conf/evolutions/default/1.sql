# Users schema

# --- !Ups

CREATE TABLE Person (
    id serial,
    name text,
    age text
);

# --- !Downs

DROP TABLE Person