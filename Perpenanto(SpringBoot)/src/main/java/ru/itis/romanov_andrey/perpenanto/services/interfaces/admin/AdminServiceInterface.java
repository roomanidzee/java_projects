package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

/**
 * 24.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AdminServiceInterface {

    void createTempPassword(Long userId);
    void generateHash(User user);

}
