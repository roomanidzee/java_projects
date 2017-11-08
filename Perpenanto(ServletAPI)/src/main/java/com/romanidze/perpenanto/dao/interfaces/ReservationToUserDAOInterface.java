package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.ReservationToUser;

import java.util.List;

public interface ReservationToUserDAOInterface extends CrudDAOInterface<ReservationToUser, Long>{

    List<ReservationToUser> findAllByUserId(Long userId);

}
