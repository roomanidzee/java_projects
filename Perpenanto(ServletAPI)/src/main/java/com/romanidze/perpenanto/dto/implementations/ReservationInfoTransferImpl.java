package com.romanidze.perpenanto.dto.implementations;

import com.romanidze.perpenanto.dto.interfaces.ReservationInfoTransferInterface;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.ReservationInfo;
import com.romanidze.perpenanto.models.temp.TempReservationInfo;
import com.romanidze.perpenanto.utils.Increase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReservationInfoTransferImpl implements ReservationInfoTransferInterface{

    private Increase mathAction = new Increase();

    @Override
    public List<TempReservationInfo> getTempReservationInfos(List<ReservationInfo> oldList) {

        long count = 0;
        int listSize = oldList.size();
        int countSize = oldList.stream()
                               .mapToInt(reservationInfo -> reservationInfo.getReservationProducts().size())
                               .sum();

        List<Long> ids = IntStream.range(0, countSize)
                                  .mapToObj(i -> this.mathAction.incrementLong(count))
                                  .collect(Collectors.toList());

        List<Long> profileIds = oldList.stream()
                                       .map(reservationInfo -> reservationInfo.getUserProfile().getId())
                                       .collect(Collectors.toList());

        List<Long> reservationIds = oldList.stream()
                                           .map(reservationInfo -> reservationInfo.getUserReservation().getId())
                                           .collect(Collectors.toList());

        Map<Long, List<Product>> productsMap = new HashMap<>();

        for(int i = 0; i < listSize; i++){
            productsMap.put(profileIds.get(i), oldList.get(i).getReservationProducts());
        }

        List<TempReservationInfo> resultList = IntStream.range(0, countSize)
                                                        .mapToObj(i -> new TempReservationInfo(
                                                                                               ids.get(i),
                                                                                                 (long) 0,
                                                                                                 (long) 0,
                                                                                                 (long) 0
                                                                )
                                                        )
                                                        .collect(Collectors.toList());

        int count11 = -1;
        int count12 = -1;
        int count13 = -1;
        int count1 = -1;

        productsMap.forEach((profileId, products) -> IntStream.range(0, products.size()).forEachOrdered(i -> {
            resultList.get(this.mathAction.incrementInt(count11)).setUserProfileId(profileId);
            resultList.get(this.mathAction.incrementInt(count12))
                                          .setUserReservationId(reservationIds.get(this.mathAction.incrementInt(count1)));
            resultList.get(this.mathAction.incrementInt(count13)).setReservationProductId(products.get(i).getId());
        }));

        return resultList;
    }
}
