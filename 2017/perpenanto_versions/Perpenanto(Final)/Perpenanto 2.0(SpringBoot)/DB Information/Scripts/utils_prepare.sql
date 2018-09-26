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

CREATE FUNCTION spended_money_on_reservations(user_number INTEGER) RETURNS INTEGER
LANGUAGE plpgsql
AS $$
BEGIN
  RETURN (
    WITH user_products AS(SELECT product_id FROM product_to_reservation
    WHERE product_to_reservation.reservation_id IN
          (SELECT reservation_id FROM reservation_to_user
          WHERE reservation_to_user.user_id = user_number
          )
    ) SELECT SUM(price) FROM product RIGHT JOIN user_products ON user_products.product_id = product.id
  );
END;
$$;

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

CREATE FUNCTION reservation_price(reservation_number INTEGER) RETURNS INTEGER
LANGUAGE plpgsql
AS $$
BEGIN
  RETURN(
    WITH reservation_products AS (SELECT product_id FROM product_to_reservation
    WHERE product_to_reservation.reservation_id = reservation_number)
    SELECT SUM(price) FROM product RIGHT JOIN reservation_products ON reservation_products.product_id = product.id
  );
END;
$$;