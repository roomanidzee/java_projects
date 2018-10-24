package com.romanidze.asyncappdbstorage.services.implementations.auth;

import com.romanidze.asyncappdbstorage.domain.Token;
import com.romanidze.asyncappdbstorage.domain.User;
import com.romanidze.asyncappdbstorage.dto.auth.AuthToken;
import com.romanidze.asyncappdbstorage.dto.auth.LoginDTO;
import com.romanidze.asyncappdbstorage.dto.auth.PersonDTO;
import com.romanidze.asyncappdbstorage.repositories.TokenRepository;
import com.romanidze.asyncappdbstorage.repositories.UserRepository;
import com.romanidze.asyncappdbstorage.security.enums.UserState;
import com.romanidze.asyncappdbstorage.services.interfaces.auth.AuthenticationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private RestTemplate restTemplate;

    @Value("${microservices.redis-service.base}")
    private String redisBaseURL;

    @Value("${microservices.redis-service.paths.ban_user}")
    private String banUserURL;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     TokenRepository tokenRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthToken loginUser(LoginDTO loginDTO) {

        String username = loginDTO.getUsername();
        String rawPassword = loginDTO.getPassword();

        Optional<User> existedUser = this.userRepository.findByUsername(username);

        if(!existedUser.isPresent()){
            throw new IllegalArgumentException("Неверный логин пользователя");
        }

        User user = existedUser.get();

        if(this.passwordEncoder.matches(rawPassword, user.getPassword())){

            Token token = this.tokenRepository.findByUser(user);

            if(token.getCreationDate().isAfter(LocalDateTime.now().minusDays(14))){

                user.setState(UserState.BANNED);
                this.userRepository.save(user);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

                PersonDTO personDTO = PersonDTO.builder()
                                               .id(String.valueOf(user.getId()))
                                               .username(user.getUsername())
                                               .tokenCreationDate(token.getCreationDate().format(formatter))
                                               .build();

                ResponseEntity<PersonDTO> personResponse = null;

                try{

                    personResponse = this.restTemplate.postForEntity(new URI(this.redisBaseURL + this.banUserURL),
                                                                      personDTO, PersonDTO.class);

                }catch (URISyntaxException e) {
                    logger.error("Пользователь не был зарегистрирован. Ошибка с путём отправки запроса.");
                }

                if(!personResponse.hasBody()){
                    throw new IllegalArgumentException("Пользователь не был забанен. Проверьте микросервис с защитой");
                }

                throw new IllegalArgumentException("Пользователь с таким токеном заблокирован в системе");

            }

            return AuthToken.mapFromToken(token);

        }else{
            throw new IllegalArgumentException("Неверный пароль пользователя");
        }

    }
}
