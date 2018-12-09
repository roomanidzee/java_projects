package com.romanidze.reactivchina.config.routes;

import com.romanidze.reactivchina.handlers.UserHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;

/**
 * 09.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class RoutesConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler){

        return RouterFunctions.route(RequestPredicates.GET("/users"), userHandler::all)
                              .andRoute(RequestPredicates.POST("/users/create"), userHandler::create)
                              .andRoute(RequestPredicates.GET("/users/{id}"), userHandler::get)
                              .andRoute(RequestPredicates.PUT("/users/update"), userHandler::update)
                              .andRoute(RequestPredicates.DELETE("/users/delete/{id}"), userHandler::delete);

    }

}
