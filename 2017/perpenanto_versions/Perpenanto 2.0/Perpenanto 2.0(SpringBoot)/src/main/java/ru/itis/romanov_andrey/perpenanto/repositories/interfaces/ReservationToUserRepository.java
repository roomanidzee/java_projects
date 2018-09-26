package ru.itis.romanov_andrey.perpenanto.repositories.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.ReservationToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.util.List;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationToUserRepository extends CrudDAOInterface<ReservationToUser, Long>{

    void delete(Long id1, Long id2);

}
