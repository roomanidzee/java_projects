package com.romanidze.asyncappdbstorage.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

@SpringBootApplication
@ComponentScan({"com.romanidze.asyncappdbstorage.config", "com.romanidze.asyncappdbstorage.controllers",
                "com.romanidze.asyncappdbstorage.services", "com.romanidze.asyncappdbstorage.security",
                "com.romanidze.asyncappdbstorage.validation"})
@EnableJpaRepositories(basePackages = {"com.romanidze.asyncappdbstorage.repositories"})
@EnableEurekaClient
@EntityScan(basePackages = {"com.romanidze.asyncappdbstorage.domain"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
