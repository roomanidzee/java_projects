package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationForm;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationAdminServiceInterface {

    void addReservation(ReservationForm reservationForm);
    void updateReservation(ReservationForm reservationForm);
    void deleteReservation(Long id);
}
