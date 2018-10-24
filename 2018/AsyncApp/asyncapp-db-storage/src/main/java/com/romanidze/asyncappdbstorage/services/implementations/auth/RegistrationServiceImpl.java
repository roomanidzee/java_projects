package com.romanidze.asyncappdbstorage.services.implementations.auth;

import com.romanidze.asyncappdbstorage.domain.RoleType;
import com.romanidze.asyncappdbstorage.domain.Token;
import com.romanidze.asyncappdbstorage.domain.User;
import com.romanidze.asyncappdbstorage.dto.auth.RegistrationDTO;
import com.romanidze.asyncappdbstorage.dto.user.UserRetrieveDTO;
import com.romanidze.asyncappdbstorage.repositories.RoleTypeRepository;
import com.romanidze.asyncappdbstorage.repositories.TokenRepository;
import com.romanidze.asyncappdbstorage.repositories.UserRepository;
import com.romanidze.asyncappdbstorage.security.enums.Role;
import com.romanidze.asyncappdbstorage.security.enums.UserState;
import com.romanidze.asyncappdbstorage.services.interfaces.auth.RegistrationService;
import com.romanidze.asyncappdbstorage.utils.RolesFormatter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final RoleTypeRepository roleTypeRepository;
    private final TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository,
                                   RoleTypeRepository roleTypeRepository,
                                   TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleTypeRepository = roleTypeRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserRetrieveDTO registerUser(RegistrationDTO registrationDTO) {

        RoleType roleType = this.roleTypeRepository.findByRole(Role.ANONYMOUS);

        User user = User.builder()
                        .username(registrationDTO.getUsername())
                        .password(registrationDTO.getPassword())
                        .roles(Collections.singleton(roleType))
                        .state(UserState.CONFIRMED)
                        .build();

        this.userRepository.save(user);

        String rolesString = RolesFormatter.getRolesString(user);

        String jwtToken = Jwts.builder()
                              .claim("roles", rolesString)
                              .claim("username", user.getUsername())
                              .setSubject(user.getId().toString())
                              .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                              .compact();

        Token token = Token.builder()
                           .value(jwtToken)
                           .creationDate(LocalDateTime.now(ZoneId.of("Europe/Moscow")))
                           .user(user)
                           .build();

        this.tokenRepository.save(token);

        return UserRetrieveDTO.builder()
                              .username(user.getUsername())
                              .imageURL("")
                              .build();
    }

}
