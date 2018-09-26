package io.vscale.perpenanto.services.interfaces.auth;

import io.vscale.perpenanto.models.usermodels.User;
import org.springframework.security.core.Authentication;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AuthenticationService {
    User getUserByAuthentication(Authentication authentication);
}
