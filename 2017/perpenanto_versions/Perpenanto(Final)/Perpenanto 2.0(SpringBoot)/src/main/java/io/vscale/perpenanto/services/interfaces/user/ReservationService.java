package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.models.usermodels.Reservation;

import java.sql.Timestamp;
import java.util.List;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationService {

    List<Reservation> getReservations();
    List<Reservation> getReservationsByCookie(String cookieValue);

    void saveReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);

    Reservation getReservationByTimestamp(Timestamp timestamp);

}
