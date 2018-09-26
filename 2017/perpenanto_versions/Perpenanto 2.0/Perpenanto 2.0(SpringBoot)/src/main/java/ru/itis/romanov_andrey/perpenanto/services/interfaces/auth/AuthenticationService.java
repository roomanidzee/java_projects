package ru.itis.romanov_andrey.perpenanto.services.interfaces.auth;

import org.springframework.security.core.Authentication;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AuthenticationService {
    User getUserByAuthentication(Authentication authentication);
}
