package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationToUserForm;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ReservationToUserAdminServiceInterface {

    void workWithReservationToUser(ReservationToUserForm reservationToUserForm, String actionType);
    void addReservationToUser(ReservationToUserForm reservationToUserForm);
    void updateReservationToUser(ReservationToUserForm reservationToUserForm);
    void deleteReservationToUser(Long orderId);

}
