package ru.itis.romanov_andrey.perpenanto.repositories.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;

import java.sql.Timestamp;
import java.util.List;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationRepository extends CrudDAOInterface<Reservation, Long>{

    List<Reservation> findAllByCreatedAt(Timestamp createdAt);
    List<Reservation> findAllByStatus(String status);
    Reservation findByCreatedAt(Timestamp createdAt);

}
