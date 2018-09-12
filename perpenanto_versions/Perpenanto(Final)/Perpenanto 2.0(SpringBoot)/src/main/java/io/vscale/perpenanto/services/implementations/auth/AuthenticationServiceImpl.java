package io.vscale.perpenanto.services.implementations.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.security.details.UserDetailsImpl;
import io.vscale.perpenanto.services.interfaces.auth.AuthenticationService;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByAuthentication(Authentication authentication) {

        UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUserModel = currentUserDetails.getUser();
        Long currentUserId = currentUserModel.getId();
        return this.userRepository.find(currentUserId);

    }
}
