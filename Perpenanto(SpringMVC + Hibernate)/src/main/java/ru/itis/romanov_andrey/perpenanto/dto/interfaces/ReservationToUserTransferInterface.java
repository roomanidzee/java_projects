package ru.itis.romanov_andrey.perpenanto.dto.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationToUser;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationToUserTransferInterface {

    List<ReservationToUser> getReservationsToUsers(List<User> users);

}
