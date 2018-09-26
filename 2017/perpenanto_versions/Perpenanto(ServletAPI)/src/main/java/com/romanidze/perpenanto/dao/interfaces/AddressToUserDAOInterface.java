package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.AddressToUser;

import java.util.List;

public interface AddressToUserDAOInterface extends CrudDAOInterface<AddressToUser, Long>{

    List<AddressToUser> findByUserId(Long userId);

}
