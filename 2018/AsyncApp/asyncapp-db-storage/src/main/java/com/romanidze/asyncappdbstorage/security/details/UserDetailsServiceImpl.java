package com.romanidze.asyncappdbstorage.security.details;

import com.romanidze.asyncappdbstorage.domain.User;
import com.romanidze.asyncappdbstorage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service("restUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> existedUser = this.userRepository.findByUsername(username);

        if(!existedUser.isPresent()){
            throw new BadCredentialsException("User not found by token <" + username + ">");
        }

        return new UserDetailsImpl(existedUser.get());

    }

}
