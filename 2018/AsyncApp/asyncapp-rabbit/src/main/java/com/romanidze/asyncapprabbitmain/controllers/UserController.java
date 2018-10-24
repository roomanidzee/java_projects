package com.romanidze.asyncapprabbitmain.controllers;

import com.romanidze.asyncapprabbitmain.dto.UserDTO;
import com.romanidze.asyncapprabbitmain.dto.UserRetrieveDTO;
import com.romanidze.asyncapprabbitmain.services.interfaces.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<String> registerWithRabbit(@RequestBody UserDTO userDTO){

        this.userService.registerWithRabbit(userDTO);

        return ResponseEntity.ok("{\"message\": \"Зарегистрировано\"");

    }

    @PostMapping(value = "/add_image_to_user", produces = "application/json")
    public ResponseEntity<String> saveUserWithImage(@RequestBody UserRetrieveDTO userRetrieveDTO){

        this.userService.addImageToUser(userRetrieveDTO);

        return ResponseEntity.ok("{\"message\": \"Добавлено\"");

    }

}
