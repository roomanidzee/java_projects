package ru.itis.romanov_andrey.perpenanto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;

import java.sql.Timestamp;
import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    List<Reservation> findAllByCreatedAt(Timestamp createdAt);
    List<Reservation> findAllByStatus(String status);
    Reservation findByCreatedAt(Timestamp createdAt);

}
