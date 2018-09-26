package com.romanidze.perpenanto.dto.implementations;

import com.romanidze.perpenanto.dto.interfaces.ReservationToUserTransferInterface;
import com.romanidze.perpenanto.models.Reservation;
import com.romanidze.perpenanto.models.ReservationToUser;
import com.romanidze.perpenanto.models.temp.TempReservationToUser;
import com.romanidze.perpenanto.utils.Increase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReservationToUserTransferImpl implements ReservationToUserTransferInterface{

    private Increase mathAction = new Increase();

    @Override
    public List<TempReservationToUser> getTempReservationToUsers(List<ReservationToUser> oldList) {

        long count = 0;
        int listSize = oldList.size();
        int countSize = oldList.stream()
                               .mapToInt(reservationToUser -> reservationToUser.getUserReservations().size())
                               .sum();

        List<Long> ids = IntStream.range(0, countSize)
                                  .mapToObj(i -> this.mathAction.incrementLong(count))
                                  .collect(Collectors.toList());

        List<Long> userIds = oldList.stream()
                                    .map(reservationToUser -> reservationToUser.getUser().getId())
                                    .collect(Collectors.toList());

        Map<Long, List<Reservation>> reservationsMap = new HashMap<>();

        for(int i = 0; i < listSize; i++){
            reservationsMap.put(userIds.get(i), oldList.get(i).getUserReservations());
        }

        List<TempReservationToUser> resultList = IntStream.range(0, countSize)
                                                          .mapToObj(
                                                                  i -> new TempReservationToUser(ids.get(i), (long)0, (long)0)
                                                          )
                                                          .collect(Collectors.toList());

        int count1 = -1;
        int count2 = -1;

        reservationsMap.forEach((userId, reservations) -> IntStream.range(0, reservations.size()).forEachOrdered(i -> {
            resultList.get(this.mathAction.incrementInt(count1)).setUserId(userId);
            resultList.get(this.mathAction.incrementInt(count2)).setUserReservationId(reservations.get(i).getId());
        }));

        return resultList;

    }
}
