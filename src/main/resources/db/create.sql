SET MODE PostgreSQL;

CREATE DATABASE shopping;
CREATE TABLE IF NOT EXISTS stores
(
    id      int PRIMARY KEY auto_increment,
    name    VARCHAR,
    brand VARCHAR,
    quantity VARCHAR,
    address VARCHAR,
    website VARCHAR
);

CREATE TABLE IF NOT EXISTS items
(
    id      int PRIMARY KEY auto_increment,
    name    VARCHAR,
    price   INTEGER,
    storeid INTEGER
);

CREATE TABLE IF NOT EXISTS stores_items
(
    id int PRIMARY KEY auto_increment,
    itemid INTEGER,
    storeid INTEGER
)

