package com.romanidze.siteinfo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 03.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.romanidze.siteinfo.controllers", "com.romanidze.siteinfo.services",
                "com.romanidze.siteinfo.config", "com.romanidze.siteinfo.repositories"})
@EnableWebFlux
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
