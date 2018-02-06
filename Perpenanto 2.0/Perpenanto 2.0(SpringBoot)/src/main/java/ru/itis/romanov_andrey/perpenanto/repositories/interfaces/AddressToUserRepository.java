package ru.itis.romanov_andrey.perpenanto.repositories.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressToUserRepository extends CrudDAOInterface<AddressToUser, Long>{

    void delete(Long id1, Long id2);

}
