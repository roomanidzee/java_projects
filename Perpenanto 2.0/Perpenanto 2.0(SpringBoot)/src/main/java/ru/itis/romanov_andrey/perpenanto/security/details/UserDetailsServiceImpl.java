package ru.itis.romanov_andrey.perpenanto.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;

import java.util.Optional;

/**
 * 28.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<User> user = this.userRepository.findByLogin(login);

        if(!user.isPresent()){
            throw new IllegalArgumentException("User not found by login <" + login + ">");
        }

        return new UserDetailsImpl(user.get());

    }
}
