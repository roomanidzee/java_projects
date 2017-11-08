package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.Reservation;

import java.sql.Timestamp;

public interface ReservationDAOInterface extends CrudDAOInterface<Reservation, Long>{

    Reservation findByTimestamp(Timestamp timestamp);

}
