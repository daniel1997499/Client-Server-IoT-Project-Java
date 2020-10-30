DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL
);

INSERT INTO user (name) VALUES
  ('Andrei'),
  ('Dan'),
  ('Dragos'),
  ('Ierdna'),
  ('Nad'),
  ('Sogard');