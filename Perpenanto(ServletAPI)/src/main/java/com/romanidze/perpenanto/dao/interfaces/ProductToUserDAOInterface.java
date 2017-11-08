package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.ProductToUser;

import java.util.List;

public interface ProductToUserDAOInterface extends CrudDAOInterface<ProductToUser, Long>{

    List<ProductToUser> findAllByUser(Long userId);
}
