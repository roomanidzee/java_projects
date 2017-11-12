package ru.itis.romanov_andrey.perpenanto.dao.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.Reservation;

import java.sql.Timestamp;
import java.util.List;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationDAOInterface extends CrudDAOInterface<Reservation, Long>{

    List<Reservation> findAllByCreatedAt(Timestamp createdAt);
    List<Reservation> findAllByStatus(String status);

}
