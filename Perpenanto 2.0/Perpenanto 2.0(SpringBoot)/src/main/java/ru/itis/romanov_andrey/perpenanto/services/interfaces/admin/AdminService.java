package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AdminService {

    void createTempPassword(Long userId);
    void generateConfirmLink(User user);

}
