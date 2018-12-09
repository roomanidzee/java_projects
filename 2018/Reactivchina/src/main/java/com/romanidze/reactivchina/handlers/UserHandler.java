package com.romanidze.reactivchina.handlers;

import com.romanidze.reactivchina.dto.UserDTO;
import com.romanidze.reactivchina.services.interfaces.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 09.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class UserHandler {

    private final UserService userService;

    @Autowired
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> all(ServerRequest req){
        return ServerResponse.ok().body(this.userService.getAllUsers(), UserDTO.class);
    }

    public Mono<ServerResponse> get(ServerRequest req){

         return this.userService.getByID(Long.parseLong(req.pathVariable("id")))
                                .flatMap(user -> ServerResponse.ok().body(Mono.just(user), UserDTO.class));

    }

    public Mono<ServerResponse> create(ServerRequest req){

        return req.bodyToMono(UserDTO.class)
                  .flatMap(user -> ServerResponse.ok().body(this.userService.saveUser(user), Integer.class));

    }

    public Mono<ServerResponse> update(ServerRequest req){

        return req.bodyToMono(UserDTO.class)
                  .flatMap(user -> ServerResponse.ok().body(this.userService.updateUser(user), Integer.class));

    }

    public Mono<ServerResponse> delete(ServerRequest req){

        return ServerResponse.ok()
                             .body(this.userService.deleteUser(Long.parseLong(req.pathVariable("id"))),
                                     Integer.class);

    }

}
