package ru.itis.romanov_andrey.perpenanto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductRepository extends JpaRepository<Product, Long>{

    Product findByTitle(String title);
    List<Product> findAllByPriceBetween(Integer start, Integer end);
    List<Product> findAllByReservations(List<Reservation> reservations);

}
