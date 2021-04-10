DROP ALL OBJECTS;

CREATE TABLE users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(250) NOT NULL,
	enabled boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE authorities (
    ID INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL
);

INSERT INTO users(username, password, enabled) VALUES ('admin', '$2a$10$8YBi4Btx55YS/0QxMCziwuZqmJBNwXRgIHxcvS9BIxOgF8cu/7LGa', true); --123
INSERT INTO users(username, password, enabled) VALUES ('bob', '$2a$10$yrY5niUqTSSGmGSULvZth..3qs2jCJOIsEwBMNkvzLwUzh6Fey7Ri', true); --234

INSERT INTO authorities(username,authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities(username,authority) VALUES ('bob', 'ROLE_USER');