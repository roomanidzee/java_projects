package com.romanidze.perpenanto.dto.interfaces;

import com.romanidze.perpenanto.models.ReservationInfo;
import com.romanidze.perpenanto.models.temp.TempReservationInfo;

import java.util.List;

public interface ReservationInfoTransferInterface {

    List<TempReservationInfo> getTempReservationInfos(List<ReservationInfo> oldList);

}
