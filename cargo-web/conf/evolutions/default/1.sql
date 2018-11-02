# --- First database schema

# --- !Ups

CREATE TABLE player (
   player_id BIGSERIAL PRIMARY KEY,
   fullname TEXT NOT NULL
);

CREATE TABLE company (
   company_id BIGSERIAL PRIMARY KEY,
   owner_id BIGINT REFERENCES player (player_id),
   company_name TEXT NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS player;
