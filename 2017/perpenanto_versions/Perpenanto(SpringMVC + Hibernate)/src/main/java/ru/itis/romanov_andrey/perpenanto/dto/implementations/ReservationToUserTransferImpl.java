package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.ReservationToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationToUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ReservationToUserTransferImpl implements ReservationToUserTransferInterface {

    @Override
    public List<ReservationToUser> getReservationsToUsers(List<User> users) {

        List<ReservationToUser> resultList = new ArrayList<>();

        int listSize = users.size();

        IntStream.range(0, listSize).forEachOrdered(i -> {

            User user = users.get(i);
            Set<Reservation> reservations = user.getReservations();

            reservations.stream()
                        .map(reservation -> ReservationToUser.builder()
                                                             .userId(user.getId())
                                                             .userReservationId(reservation.getId())
                                                             .build()
                        )
                        .forEachOrdered(resultList::add);

        });

        return resultList;

    }

}
