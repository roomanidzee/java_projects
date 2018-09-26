package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ReservationToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ReservationToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ReservationToUserDTO implements EntityDTOInterface<ReservationToUserTransfer, ReservationToUser>{

    @Override
    public List<ReservationToUserTransfer> convert(List<ReservationToUser> originalList) {

        List<ReservationToUserTransfer> resultList = new ArrayList<>();

        originalList.forEach(reservationToUser -> {

            User user = reservationToUser.getUser();
            Set<Reservation> reservations = reservationToUser.getReservations();

            reservations.stream()
                        .map(reservation -> ReservationToUserTransfer.builder()
                                                                     .userId(user.getId())
                                                                     .reservationId(reservation.getId())
                                                                     .build()
                        )
                        .forEachOrdered(resultList::add);

        });

        return resultList;

    }
}
