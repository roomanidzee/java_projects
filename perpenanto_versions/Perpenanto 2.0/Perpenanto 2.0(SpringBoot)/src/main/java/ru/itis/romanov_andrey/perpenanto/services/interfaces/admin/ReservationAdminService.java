package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationAdminService {

    void addReservation(ReservationForm reservationForm);
    void updateReservation(ReservationForm reservationForm);
    void deleteReservation(Long id);

}
