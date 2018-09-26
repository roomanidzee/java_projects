package ru.itis.romanov_andrey.perpenanto.services.interfaces.auth;

import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface RegistrationService {
    void register(UserRegistrationForm userForm);
}
