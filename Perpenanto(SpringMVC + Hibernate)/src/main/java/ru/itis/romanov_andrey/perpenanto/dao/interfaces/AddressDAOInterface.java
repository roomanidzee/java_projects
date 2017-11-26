package ru.itis.romanov_andrey.perpenanto.dao.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.Address;
import ru.itis.romanov_andrey.perpenanto.models.User;

import java.util.List;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressDAOInterface extends CrudDAOInterface<Address, Long>{

    List<Address> findByUsers(List<User> users);
    Address findByPostalCode(Integer postalCode);

}
