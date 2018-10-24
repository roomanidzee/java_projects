package com.romanidze.asyncappdbstorage.controllers;

import com.romanidze.asyncappdbstorage.dto.auth.AuthToken;
import com.romanidze.asyncappdbstorage.dto.auth.LoginDTO;
import com.romanidze.asyncappdbstorage.dto.auth.RegistrationDTO;
import com.romanidze.asyncappdbstorage.dto.user.UserRetrieveDTO;
import com.romanidze.asyncappdbstorage.services.interfaces.auth.AuthenticationService;
import com.romanidze.asyncappdbstorage.services.interfaces.auth.RegistrationService;
import com.romanidze.asyncappdbstorage.services.interfaces.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationService authenticationService,
                          RegistrationService registrationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRetrieveDTO> registerUser(@RequestBody RegistrationDTO registrationDTO){

        UserRetrieveDTO userRetrieveDTO = this.registrationService.registerUser(registrationDTO);

        return ResponseEntity.ok(userRetrieveDTO);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> loginUser(@Valid @RequestBody LoginDTO loginDTO){

        AuthToken authToken = this.authenticationService.loginUser(loginDTO);

        return ResponseEntity.ok(authToken);

    }

    @PostMapping(value = "/add_image_to_user", produces = "application/json")
    public ResponseEntity<String> addImageToUser(@RequestBody UserRetrieveDTO userRetrieveDTO){

        this.userService.saveImageToUser(userRetrieveDTO);

        return ResponseEntity.ok("{\"message\": \"Картинка сохранена\"");

    }

    @GetMapping("/get_users")
    public ResponseEntity<List<UserRetrieveDTO>> getUsers(Authentication authentication){

        List<UserRetrieveDTO> users = this.userService.getAllUsersWithImages();

        return ResponseEntity.ok(users);

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<UserRetrieveDTO>> getUserById(@PathVariable("id") Long userID){

        List<UserRetrieveDTO> userInfo = this.userService.getUserByID(userID);

        return ResponseEntity.ok(userInfo);

    }
}
