package ru.itis.romanov_andrey.perpenanto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressRepository extends JpaRepository<Address, Long>{

    List<Address> findByUsers(List<User> users);
    Address findByPostalCode(Integer postalCode);

}
