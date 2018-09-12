package io.vscale.perpenanto.security.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * 28.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider{

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserAuthenticationProvider(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> userOptional = this.userRepository.findByLogin(username);

        if(!userOptional.isPresent()){
            throw new BadCredentialsException("Wrong password or login");
        }

        userOptional.ifPresent(user -> {

            if(!passwordEncoder.matches(password, user.getPassword())){

                if(passwordEncoder.matches(password, user.getTempPassword())){

                    user.setTempPassword(null);
                    this.userRepository.update(user);

                }else{
                    throw new BadCredentialsException("Wrong password or login");
                }

            }

        });

        UserDetails details = this.userDetailsService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        return new UsernamePasswordAuthenticationToken(details, password, authorities);

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(UsernamePasswordAuthenticationToken.class.getName());
    }
}
