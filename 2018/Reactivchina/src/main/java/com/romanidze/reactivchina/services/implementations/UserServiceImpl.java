package com.romanidze.reactivchina.services.implementations;

import com.romanidze.reactivchina.domain.User;
import com.romanidze.reactivchina.dto.UserDTO;
import com.romanidze.reactivchina.repositories.interfaces.UserRepository;
import com.romanidze.reactivchina.services.interfaces.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 09.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserDTO> getAllUsers() {
        return this.userRepository.findAll()
                   .map(UserDTO::mapFromUser);
    }

    @Override
    public Mono<UserDTO> getByID(Long userID) {
        return this.userRepository.find(userID)
                                  .map(UserDTO::mapFromUser);
    }

    @Override
    public Flux<Integer> saveUser(UserDTO model) {

        User user = UserDTO.mapFromUserDTO(model);
        return this.userRepository.save(user);
    }

    @Override
    public Flux<Integer> updateUser(UserDTO model) {

        User user = UserDTO.mapFromUserDTO(model);
        return this.userRepository.update(user);
    }

    @Override
    public Flux<Integer> deleteUser(Long userID) {

        return this.userRepository.delete(userID);

    }
}
