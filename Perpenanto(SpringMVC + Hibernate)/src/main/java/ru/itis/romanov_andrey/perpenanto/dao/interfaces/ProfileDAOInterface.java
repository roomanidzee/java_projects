package ru.itis.romanov_andrey.perpenanto.dao.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.Profile;
import ru.itis.romanov_andrey.perpenanto.models.User;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileDAOInterface extends CrudDAOInterface<Profile, Long>{

    Profile findByUser(User user);

}
