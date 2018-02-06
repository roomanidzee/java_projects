CREATE FUNCTION solded_products_count( user_number INTEGER ) RETURNS INTEGER AS
$$
BEGIN
  RETURN (
    SELECT COUNT(*) FROM product_to_reservation WHERE product_to_reservation.product_id IN (
                             SELECT product_id FROM product_to_user WHERE product_to_user.user_id = user_number
    )
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
                                   (SELECT product_id FROM product_to_reservation
                                     WHERE product_to_reservation.reservation_id IN
                                           (SELECT reservation_id FROM reservation_to_user
                                               WHERE reservation_to_user.user_id = user_number
                                           )
                                   )
      ) AS pr
  );
END;
$$
LANGUAGE PLPGSQL;

CREATE FUNCTION count_reservations(user_number INTEGER) RETURNS INTEGER AS
$$
BEGIN
  RETURN(
    SELECT COUNT(*) FROM reservation_to_user WHERE reservation_to_user.user_id = user_number
  );
END;
$$
LANGUAGE PLPGSQL;

CREATE FUNCTION get_common_products_price(user_number INTEGER) RETURNS INTEGER AS
$$
BEGIN
  RETURN(
    SELECT SUM(price) FROM
      (
        SELECT * FROM product WHERE product.id IN (
            SELECT product_id FROM product_to_user WHERE product_to_user.user_id = user_number
          )
      )AS pr1
  );
END;
$$
LANGUAGE PLPGSQL;