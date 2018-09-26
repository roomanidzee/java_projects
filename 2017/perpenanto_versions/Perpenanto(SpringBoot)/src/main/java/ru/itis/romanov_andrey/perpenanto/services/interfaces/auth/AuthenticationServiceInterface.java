package ru.itis.romanov_andrey.perpenanto.services.interfaces.auth;

import org.springframework.security.core.Authentication;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AuthenticationServiceInterface {
    User getUserByAuthentication(Authentication authentication);
}
