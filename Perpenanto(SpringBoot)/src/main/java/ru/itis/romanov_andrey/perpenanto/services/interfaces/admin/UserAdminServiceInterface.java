package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.UserForm;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface UserAdminServiceInterface {

    void workWithUser(UserForm userForm, String actionType);
    void addUser(UserForm userForm);
    void updateUser(UserForm userForm);
    void deleteUser(Long id);

}
