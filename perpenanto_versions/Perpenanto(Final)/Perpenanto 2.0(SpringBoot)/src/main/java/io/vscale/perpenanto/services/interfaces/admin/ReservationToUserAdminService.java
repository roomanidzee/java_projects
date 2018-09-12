package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.forms.admin.ReservationToUserForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationToUserAdminService {

    void addReservationToUser(ReservationToUserForm reservationToUserForm);
    void updateReservationToUser(ReservationToUserForm reservationToUserForm);
    void deleteReservationToUser(Long userId, Long orderId);

}
