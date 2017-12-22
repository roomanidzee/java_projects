package ru.itis.romanov_andrey.perpenanto.services.interfaces.auth;

import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface RegistrationServiceInterface {
    void register(UserRegistrationForm userForm);
}
