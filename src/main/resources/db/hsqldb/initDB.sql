DROP TABLE account IF EXISTS;

CREATE TABLE account (
  id         INTEGER IDENTITY PRIMARY KEY,
  name 		 VARCHAR(30),
  address    VARCHAR(255),
  telephone  VARCHAR(20),
  email		 varchar(30)
);
CREATE INDEX account_name_index ON account (name);
