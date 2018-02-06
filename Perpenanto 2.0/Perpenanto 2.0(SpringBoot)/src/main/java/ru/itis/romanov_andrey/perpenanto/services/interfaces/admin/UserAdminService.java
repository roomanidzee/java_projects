package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.UserForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface UserAdminService {

    void addUser(UserForm userForm);
    void updateUser(UserForm userForm);
    void deleteUser(Long id);

}
