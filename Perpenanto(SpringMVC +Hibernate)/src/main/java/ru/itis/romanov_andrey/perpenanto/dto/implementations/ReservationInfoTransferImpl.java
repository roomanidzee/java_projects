package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.ReservationInfoTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.Product;
import ru.itis.romanov_andrey.perpenanto.models.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ReservationInfoTransferImpl implements ReservationInfoTransferInterface {

    @Override
    public List<ReservationInfo> getReservationInfos(List<User> users) {

        List<ReservationInfo> resultList = new ArrayList<>();

        users.forEach(user -> {

            Set<Reservation> reservations1 = user.getReservations();

            reservations1.forEach(reservation -> {

                List<Product> products = reservation.getProducts();
                products.stream()
                        .map(product -> ReservationInfo.builder()
                                                       .userId(user.getId())
                                                       .userReservationId(reservation.getId())
                                                       .reservationProductId(product.getId())
                                                       .build()
                        )
                        .forEachOrdered(resultList::add);

            });
        });

        return resultList;

    }
}
