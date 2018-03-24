package ru.itis.romanov_andrey.perpenanto.dao.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.User;

import java.util.Optional;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface UserDAOInterface extends CrudDAOInterface<User, Long>{

    User findByUsername(String username);

}
