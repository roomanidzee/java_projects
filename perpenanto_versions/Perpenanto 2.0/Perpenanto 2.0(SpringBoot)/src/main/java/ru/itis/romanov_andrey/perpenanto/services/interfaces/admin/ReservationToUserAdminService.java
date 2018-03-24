package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationToUserForm;

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
