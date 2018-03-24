--busket - служебная таблица, данные в неё добавляются по мере поступления,
--и удаляются после оформления заказа

INSERT INTO "user" (username_or_email, "password")
                   VALUES('example1@gmail.com', '$2a$10$p4FJnopLUUQFgSoia3sMe.3DNj0j8Yd4CoRHJuvyM22fhfVe/oGN2');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (1, 'Россия', 420033, 'Казань', 'Сабан', 5);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Андрей', 'Романов', 1, 1);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example2@gmail.com', '$2a$10$18ya0oiFp.ReM3/oc9L.de9upFe1cnmzhR54NgeOwTbVXMnFK3MHK');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (2, 'Азейрбаджан', 246810, 'Баку', 'Баиловская', 4);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Адиль', 'Абдуррахман', 2, 2);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example3@gmail.com', '$2a$10$tjwpsfm2TMLL37mS60RptOfaou3xQuhDkOISaPnaMw1pFQ3vjbikO');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (3, 'Армения', 173925, 'Ереван', 'Акобяна', 7);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Давид', 'Ровиташвили', 3, 3);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example4@gmail.com', '$2a$10$elvG9k4W7wE1hf5k9ifOC.yRKwoyo6QRMYDr44z0la7RW9JvLpbgG');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (4, 'Беларусь', 289015, 'Минск', 'Васнецова', 2);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Олег', 'Джокович', 4, 4);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example5@gmail.com', '$2a$10$EcTmqVaErpIH3lxXzvOwVepEyWRwyfTx2fgoxhZjsiLOgQYa47SOi');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (5, 'Казахстан', 491820, 'Астана', 'Аксай', 3);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Санжар', 'Ендылбеков', 5, 5);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example6@gmail.com', '$2a$10$xGe8uiOcY9VPyW2TrIuj7uPkG0b0lyJDk6Ba1OOdLkX/3zeO1VKi.');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (6, 'Киргизия', 723940, 'Бишкек', 'Керамическая', 3);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Юсуф', 'Файхразиев', 6, 6);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example7@gmail.com', '$2a$10$dhOi0v1icBfRdsYUyeVSQuvGRHfEXsnBRkT6emHwPToReFXqyL0cu');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (7, 'Молдавия', 196025, 'Кишенев', 'Mateevici', 1);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Михаил', 'Кику', 7, 7);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example8@gmail.com', '$2a$10$7gpCOxU2wwQldE9WNzo5BuaSX38GtcF8plum9fzhGhDxcDtqjwUNe');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (8, 'Таджикистан', 111111, 'Душанбе', 'Карабаева', 2);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Заур', 'Такбанов', 8, 8);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example9@gmail.com', '$2a$10$hjqD8NF4P97zIVQu8xhKFOb.sNbeFrOM7m7hZ71ChrGacLe/Y3AE.');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (9, 'Туркменистан', 304050, 'Ашхабад', 'Кемине', 10);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Умар', 'Джарджонов', 9, 9);
INSERT INTO "user" (username_or_email, "password")
                   VALUES('example10@gmail.com', '$2a$10$LQLwd9OxWMmfdXQb.KXNZOfRAb5IqXzCrOVE4hkpDD9BRbmP3y9hu');
INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number)
                   VALUES (10, 'Узбекистан', 112863, 'Ташкент', 'Чирчикбуйи', 8);
INSERT INTO profile(person_name, person_surname, user_id, address_id)
                   VALUES ('Карим', 'Дегтаров', 10, 10);
INSERT INTO product(title, price, description, photo_link)
                   VALUES ('Книга', 1500, 'Полезная книга', 'http://greetingbooks.com/wp-content/uploads/2015/08/3.png');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Смартфон', 4000, 'Современный телефон',
                'http://static4.businessinsider.com/image/5984c2904fc3c04f498b51a5-480/iphone-7-and-iphone-7-plus.jpg');
INSERT INTO product(title, price, description, photo_link)
                   VALUES ('Очки', 300, 'Прекрасные очки', 'https://www.39dollarglasses.com/img/frames/40171_BLACK.jpg');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Книга 2.0', 1500, 'Полезная книга', 'http://greetingbooks.com/wp-content/uploads/2015/08/3.png');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Смартфон 2.0', 4000, 'Современный телефон',
                   'http://static4.businessinsider.com/image/5984c2904fc3c04f498b51a5-480/iphone-7-and-iphone-7-plus.jpg');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Очки 2.0', 300, 'Прекрасные очки', 'https://www.39dollarglasses.com/img/frames/40171_BLACK.jpg');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Книга 3.0', 1500, 'Полезная книга', 'http://greetingbooks.com/wp-content/uploads/2015/08/3.png');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Смартфон 3.0', 4000, 'Современный телефон',
                   'http://static4.businessinsider.com/image/5984c2904fc3c04f498b51a5-480/iphone-7-and-iphone-7-plus.jpg');
INSERT INTO product(title, price, description, photo_link)
           VALUES ('Очки 3.0', 300, 'Прекрасные очки', 'https://www.39dollarglasses.com/img/frames/40171_BLACK.jpg');
INSERT INTO product(title, price, description, photo_link)
                   VALUES ('Книга S', 1500, 'Полезная книга', 'http://greetingbooks.com/wp-content/uploads/2015/08/3.png');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '2 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '3 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '4 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '5 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '6 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '7 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '8 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '9 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '10 Days', 'Отправлен');
INSERT INTO reservation(created_at, status)
           VALUES (CURRENT_TIMESTAMP + INTERVAL '11 Days', 'Отправлен');
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (1, 1);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (2, 2);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (3, 3);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (4, 4);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (5, 5);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (6, 6);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (7, 7);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (8, 8);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (9, 9);
INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES (10, 10);
INSERT INTO product_to_user(user_id, product_id) VALUES (1, 10);
INSERT INTO product_to_user(user_id, product_id) VALUES (2, 9);
INSERT INTO product_to_user(user_id, product_id) VALUES (3, 8);
INSERT INTO product_to_user(user_id, product_id) VALUES (4, 7);
INSERT INTO product_to_user(user_id, product_id) VALUES (5, 6);
INSERT INTO product_to_user(user_id, product_id) VALUES (6, 5);
INSERT INTO product_to_user(user_id, product_id) VALUES (7, 4);
INSERT INTO product_to_user(user_id, product_id) VALUES (8, 3);
INSERT INTO product_to_user(user_id, product_id) VALUES (9, 2);
INSERT INTO product_to_user(user_id, product_id) VALUES (10, 1);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (1, 1, 1);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (2, 2, 2);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (3, 3, 3);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (4, 4, 4);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (5, 5, 5);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (6, 6, 6);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (7, 7, 7);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (8, 8, 8);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (9, 9, 9);
INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES (10, 10, 10);