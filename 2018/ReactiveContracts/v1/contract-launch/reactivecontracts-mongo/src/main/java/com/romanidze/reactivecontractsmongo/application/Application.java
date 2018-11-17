package com.romanidze.reactivecontractsmongo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.romanidze.reactivecontractsmongo.controllers", "com.romanidze.reactivecontractsmongo.services",
                "com.romanidze.reactivecontractsmongo.config"})
@EnableReactiveMongoRepositories(basePackages = {"com.romanidze.reactivecontractsmongo.repositories"})
@EntityScan(basePackages = {"com.romanidze.reactivecontractsmongo.domain"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
