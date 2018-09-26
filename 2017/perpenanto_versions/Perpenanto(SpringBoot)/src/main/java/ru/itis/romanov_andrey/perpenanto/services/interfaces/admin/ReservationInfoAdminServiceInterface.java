package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationInfoForm;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationInfoAdminServiceInterface {

    void addReservationInfo(ReservationInfoForm reservationInfoForm);
    void updateReservationInfo(ReservationInfoForm reservationInfoForm);
    void deleteReservationInfo(Long productId);

}
