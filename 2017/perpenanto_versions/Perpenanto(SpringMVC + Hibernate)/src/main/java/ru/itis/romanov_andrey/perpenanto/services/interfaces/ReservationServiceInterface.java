package ru.itis.romanov_andrey.perpenanto.services.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.Reservation;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationServiceInterface {

    List<Reservation> getReservations();
    List<Reservation> getReservationsByCookie(String cookieValue);
    void saveOrUpdate(Reservation reservation);
    void delete(Long id);

}
