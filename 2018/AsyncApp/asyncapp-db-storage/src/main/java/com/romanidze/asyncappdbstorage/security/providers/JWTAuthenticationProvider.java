package com.romanidze.asyncappdbstorage.security.providers;

import com.romanidze.asyncappdbstorage.domain.User;
import com.romanidze.asyncappdbstorage.repositories.UserRepository;
import com.romanidze.asyncappdbstorage.security.authentication.JWTTokenAuthentication;
import com.romanidze.asyncappdbstorage.security.details.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LogManager.getLogger(JWTAuthenticationProvider.class);

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public JWTAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JWTTokenAuthentication jwtTokenAuthentication = (JWTTokenAuthentication) authentication;

        Claims body;

        try{
            body = Jwts.parser()
                       .setSigningKey(this.jwtSecret)
                       .parseClaimsJws(jwtTokenAuthentication.getName())
                       .getBody();

        }catch (MalformedJwtException | SignatureException e){
            logger.error("Прозошла ошибка при обработке токена: " + e);
            throw new AuthenticationServiceException("Получен некорректный токен");
        }

        Optional<User> existedUser = this.userRepository.findByUsername(body.get("username").toString());

        if(!existedUser.isPresent()){
            throw new BadCredentialsException("Пользователя с таким юзернеймом не существует");
        }

        UserDetails userDetails = new UserDetailsImpl(existedUser.get());

        jwtTokenAuthentication.setUserDetails(userDetails);
        jwtTokenAuthentication.setAuthenticated(true);
        return jwtTokenAuthentication;

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JWTTokenAuthentication.class.equals(clazz);
    }
}
