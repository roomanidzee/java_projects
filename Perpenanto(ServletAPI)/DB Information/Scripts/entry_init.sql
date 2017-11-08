CREATE TABLE "user"(
  id SERIAL PRIMARY KEY,
  username_or_email VARCHAR(255),
  password VARCHAR(255)
);

CREATE TABLE profile(
  id SERIAL PRIMARY KEY,
  person_name VARCHAR(100) NOT NULL,
  person_surname VARCHAR(100) NOT NULL,
  user_id INTEGER,
  address_id INTEGER,
  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE product(
  id SERIAL PRIMARY KEY,
  title VARCHAR(100),
  price INTEGER,
  description TEXT,
  photo_link TEXT
);

CREATE TABLE reservation(
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMP,
  status VARCHAR(20)
);