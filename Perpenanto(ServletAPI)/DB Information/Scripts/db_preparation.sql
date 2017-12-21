--Начало: команды пользователя postgres
CREATE USER perpenanto_user WITH CREATEDB LOGIN PASSWORD 'very_strong_pass';
CREATE DATABASE "perpenanto_db(Informatics)";

ALTER DATABASE "perpenanto_db(Informatics)" OWNER TO perpenanto_user;
GRANT CONNECT,CREATE ON DATABASE "perpenanto_db(Informatics)" TO perpenanto_user;

--Конец: команды пользователя postgres

--Начало: команды пользователя perpenanto_user

ALTER DEFAULT PRIVILEGES
      FOR ROLE perpenanto_user
      IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO perpenanto_user;

ALTER DEFAULT PRIVILEGES
      FOR ROLE perpenanto_user
      IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO perpenanto_user;

CREATE TABLE "user"(
  id SERIAL PRIMARY KEY,
  username_or_email VARCHAR(255) UNIQUE,
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
  title VARCHAR(100) UNIQUE,
  price INTEGER,
  description TEXT,
  photo_link TEXT,
  CONSTRAINT check_product_price CHECK(price > 100)
);

CREATE TABLE reservation(
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMP,
  status VARCHAR(20),
  CONSTRAINT check_creation_date CHECK(created_at >= TIMESTAMP '2017-11-18 00:00:00')
);

CREATE TABLE reservation_to_user(
  id SERIAL PRIMARY KEY ,
  user_id INTEGER NOT NULL,
  user_reservation_id INTEGER NOT NULL,
  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
  FOREIGN KEY (user_reservation_id) REFERENCES reservation(id) ON DELETE CASCADE
);

CREATE TABLE reservation_info(
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  reservation_id INTEGER NOT NULL,
  reservation_product_id INTEGER NOT NULL,
  CONSTRAINT fk_user_profile_id FOREIGN KEY (user_id) REFERENCES profile(id) ON DELETE CASCADE,
  CONSTRAINT fk_reservation_id FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE cascade,
  FOREIGN KEY (reservation_product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE busket(
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  reservation_product_id INTEGER NOT NULL,
  CONSTRAINT fk_user_profile_id FOREIGN KEY (user_id) REFERENCES profile(id) ON DELETE CASCADE,
  FOREIGN KEY (reservation_product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE product_to_user(
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  product_id INTEGER NOT NULL,
  CONSTRAINT fk_profile_id FOREIGN KEY (user_id) REFERENCES "user"(id),
  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE address_to_user(
  id SERIAL PRIMARY KEY,
  user_id INTEGER,
  country VARCHAR(255),
  post_index INTEGER,
  city VARCHAR(255),
  street VARCHAR(255),
  home_number SMALLINT,
  FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

ALTER TABLE profile ADD FOREIGN KEY (address_id) REFERENCES address_to_user(id);

--представления: начало

CREATE VIEW user_addresses AS SELECT * FROM address_to_user LEFT JOIN "user" ON "user".id = address_to_user.user_id;
CREATE VIEW user_products AS SELECT * FROM product_to_user LEFT JOIN "user" ON "user".id = product_to_user.user_id;

--представления: конец

--хранимые процедуры: начало

CREATE FUNCTION solded_products_count( product_number INTEGER) RETURNS INTEGER AS
$$
BEGIN
  RETURN (
    SELECT COUNT(*) FROM reservation_info WHERE reservation_info.reservation_product_id = product_number
  );
END;
$$
LANGUAGE PLPGSQL;

CREATE FUNCTION spended_money_on_reservations(user_number INTEGER) RETURNS INTEGER AS
$$
BEGIN
  RETURN (
      SELECT SUM(price) FROM
        (SELECT * FROM product WHERE product.id IN
                                     (SELECT reservation_product_id FROM reservation_info
                                           WHERE reservation_info.user_id = user_number)) AS pr
  );
END;
$$
LANGUAGE PLPGSQL;

--хранимые процедуры: конец

--тригерры: начало

CREATE OR REPLACE FUNCTION check_reservation_id() RETURNS TRIGGER AS
$$
BEGIN

  IF NEW.user_reservation_id NOT IN (SELECT user_reservation_id FROM reservation_to_user) THEN
    RETURN NEW;
  END IF;

END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER check_order_id
    BEFORE INSERT
    ON reservation_to_user
    FOR EACH ROW
    EXECUTE PROCEDURE check_reservation_id();

--тригерры: конец

--индексы: начало

CREATE INDEX users_index ON "user"(username_or_email, password);
CREATE INDEX profiles_index ON profile(person_name, person_surname, user_id);

--индексы: конец

--Конец: команды пользователя perpenanto_user