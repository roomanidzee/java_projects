package com.romanidze.reactivchina.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 08.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.romanidze.reactivchina.config", "com.romanidze.reactivchina.handlers",
                "com.romanidze.reactivchina.repositories",
                "com.romanidze.reactivchina.domain", "com.romanidze.reactivchina.services"})
@EnableWebFlux
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
