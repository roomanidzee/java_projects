package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.forms.admin.ReservationForm;

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
