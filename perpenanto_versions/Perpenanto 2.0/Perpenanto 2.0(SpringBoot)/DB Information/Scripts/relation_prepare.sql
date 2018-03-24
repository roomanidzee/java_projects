CREATE TABLE address_to_user(
  user_id INTEGER,
  address_id INTEGER,
  CONSTRAINT const_user_fk FOREIGN KEY(user_id) REFERENCES "user"(id) ON DELETE RESTRICT,
  CONSTRAINT address_fk FOREIGN KEY(address_id) REFERENCES address(id) ON DELETE RESTRICT
);

CREATE TABLE reservation_to_user(
  user_id INTEGER,
  reservation_id INTEGER,
  CONSTRAINT order_user_fk FOREIGN KEY(user_id) REFERENCES "user"(id) ON DELETE RESTRICT,
  CONSTRAINT order_fk FOREIGN KEY(reservation_id) REFERENCES reservation(id) ON DELETE RESTRICT
);

CREATE TABLE product_to_user(
  user_id INTEGER,
  product_id INTEGER,
  CONSTRAINT product_user_fk FOREIGN KEY(user_id) REFERENCES "user"(id) ON DELETE RESTRICT,
  CONSTRAINT product_fk FOREIGN KEY(product_id) REFERENCES product(id) ON DELETE RESTRICT
);

CREATE TABLE product_to_reservation(
  reservation_id INTEGER,
  product_id INTEGER,
  CONSTRAINT order_product_fk FOREIGN KEY(reservation_id) REFERENCES reservation(id) ON DELETE RESTRICT,
  CONSTRAINT product_order_fk FOREIGN KEY(product_id) REFERENCES product(id) ON DELETE RESTRICT
);

CREATE TABLE busket(
  busket_user_id INTEGER,
  busket_product_id INTEGER,
  CONSTRAINT busket_user_fk FOREIGN KEY(busket_user_id) REFERENCES profile(id) ON DELETE RESTRICT,
  CONSTRAINT busket_product_fk FOREIGN KEY(busket_product_id) REFERENCES product ON DELETE RESTRICT
);

CREATE TABLE files_to_profile(
  file_id INTEGER,
  profile_id INTEGER,
  CONSTRAINT file_fk FOREIGN KEY(file_id) REFERENCES files_of_service(id) ON DELETE RESTRICT,
  CONSTRAINT profile_fk FOREIGN KEY(profile_id) REFERENCES profile(id) ON DELETE RESTRICT
);