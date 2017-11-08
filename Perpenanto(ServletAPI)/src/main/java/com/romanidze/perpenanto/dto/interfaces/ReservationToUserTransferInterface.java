package com.romanidze.perpenanto.dto.interfaces;

import com.romanidze.perpenanto.models.ReservationToUser;
import com.romanidze.perpenanto.models.temp.TempReservationToUser;

import java.util.List;

public interface ReservationToUserTransferInterface {

    List<TempReservationToUser> getTempReservationToUsers(List<ReservationToUser> oldList);

}
