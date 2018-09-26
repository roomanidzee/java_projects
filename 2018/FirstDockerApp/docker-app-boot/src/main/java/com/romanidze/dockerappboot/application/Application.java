package com.romanidze.dockerappboot.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 23.09.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

@SpringBootApplication
@ComponentScan({"com.romanidze.dockerappboot.config", "com.romanidze.dockerappboot.domain",
                "com.romanidze.dockerappboot.receivers", "com.romanidze.dockerappboot.services"})
@EnableJpaRepositories(basePackages = {"com.romanidze.dockerappboot.repositories"})
@EntityScan(basePackages = {"com.romanidze.dockerappboot.domain"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
