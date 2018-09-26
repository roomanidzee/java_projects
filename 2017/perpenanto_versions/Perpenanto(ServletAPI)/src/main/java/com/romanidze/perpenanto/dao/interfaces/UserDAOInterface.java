package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.User;

public interface UserDAOInterface extends CrudDAOInterface<User, Long>{

    User findByUsername(String username);

}
