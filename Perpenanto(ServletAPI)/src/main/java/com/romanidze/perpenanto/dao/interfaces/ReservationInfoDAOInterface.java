package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.ReservationInfo;

import java.util.List;

public interface ReservationInfoDAOInterface extends CrudDAOInterface<ReservationInfo, Long>{

    List<ReservationInfo> findAllByUserId(Long userId);
    List<ReservationInfo> findAllByReservationId(Long reservationId);

}
