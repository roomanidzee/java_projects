CREATE TABLE "user"(
  id SERIAL PRIMARY KEY,
  login VARCHAR(100),
  password TEXT,
  password_temp TEXT,
  role VARCHAR(6),
  user_state VARCHAR(13),
  unique_confirm_hash TEXT
);

CREATE TABLE profile(
  id SERIAL PRIMARY KEY,
  user_id INTEGER,
  email VARCHAR(50),
  name VARCHAR(50),
  surname VARCHAR(50),
  phone VARCHAR(50),
  CONSTRAINT user_fk FOREIGN KEY(user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE persistent_logins(
  username VARCHAR(100) NOT NULL ,
  series VARCHAR(64) PRIMARY KEY ,
  token VARCHAR(64) NOT NULL ,
  last_used TIMESTAMP NOT NULL
);

CREATE TABLE address(
  id SERIAL PRIMARY KEY,
  country VARCHAR(50),
  postal_code INTEGER,
  city VARCHAR(50),
  street VARCHAR(50),
  home_number INTEGER
);

CREATE TABLE product(
  id SERIAL PRIMARY KEY,
  title VARCHAR(100),
  price INTEGER,
  description VARCHAR(600),
  photo_link TEXT
);

CREATE TABLE reservation(
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMP,
  status VARCHAR(30)
);

CREATE TABLE files_of_service(
  id SERIAL PRIMARY KEY,
  original_name VARCHAR(100),
  file_size INTEGER,
  encoded_name VARCHAR(50),
  file_type VARCHAR(40),
  file_url TEXT
);

CREATE INDEX product_title_search ON product(title);
CREATE INDEX product_price_search ON product(price);