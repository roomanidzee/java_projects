INSERT INTO person(id, unique_confirm_hash, password_temp, login, password, role, user_state)
  SELECT 1, '', '', 'admin', '$2a$10$s9El2R7Bxlzyf8SIS0Aex.mOI5FV6nDGW0vK5CqzqXyAIzbOwrLo6',
    'ADMIN', 'CONFIRMED'
  WHERE NOT EXISTS(SELECT id FROM person WHERE id = 1);

INSERT INTO profile(user_id, email, person_name, person_surname)
  SELECT 1, 'steampart@gmail.com', 'Андрей', 'Романов'
  WHERE NOT EXISTS(SELECT user_id FROM profile WHERE user_id = 1);

INSERT INTO address(id, city, country, home_number, postal_code, street)
  SELECT 1, 'Казань', 'Россия', 3, 420033, 'Сабан'
  WHERE NOT EXISTS(SELECT id FROM address WHERE id = 1);

INSERT INTO address_to_user(unique_user_id, address_id)
  SELECT 1, 1
  WHERE NOT EXISTS(SELECT unique_user_id FROM address_to_user WHERE unique_user_id = 1);