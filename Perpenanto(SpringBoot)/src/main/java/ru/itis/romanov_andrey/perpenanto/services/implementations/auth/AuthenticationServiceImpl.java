package ru.itis.romanov_andrey.perpenanto.services.implementations.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.details.UserDetailsImpl;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationServiceInterface;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationServiceInterface{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByAuthentication(Authentication authentication) {

        UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUserModel = currentUserDetails.getUser();
        Long currentUserId = currentUserModel.getId();
        return this.userRepository.findOne(currentUserId);

    }
}
