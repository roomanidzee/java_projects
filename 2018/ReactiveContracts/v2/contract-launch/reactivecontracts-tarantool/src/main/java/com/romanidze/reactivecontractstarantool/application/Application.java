package com.romanidze.reactivecontractstarantool.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"com.romanidze.reactivecontractstarantool.config", "com.romanidze.reactivecontractstarantool.controllers",
                "com.romanidze.reactivecontractstarantool.repositories", "com.romanidze.reactivecontractstarantool.services"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
