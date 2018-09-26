package io.vscale.perpenanto.dto.implementations;

import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.transfermodels.ReservationToUserTransfer;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.models.usermodels.ReservationToUser;
import io.vscale.perpenanto.models.usermodels.User;

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
