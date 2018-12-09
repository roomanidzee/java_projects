package com.romanidze.reactivchina.services.interfaces;

import com.romanidze.reactivchina.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 09.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface UserService {

    Flux<UserDTO> getAllUsers();
    Mono<UserDTO> getByID(Long userID);
    Flux<Integer> saveUser(UserDTO model);
    Flux<Integer> updateUser(UserDTO model);
    Flux<Integer> deleteUser(Long userID);

}
