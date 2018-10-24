package com.romanidze.asyncappredis.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * 20.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.romanidze.asyncappredis.config", "com.romanidze.asyncappredis.controllers",
                "com.romanidze.asyncappredis.services"})
@EnableRedisRepositories(basePackages = {"com.romanidze.asyncappredis.repositories"})
@EntityScan(basePackages = {"com.romanidze.asyncappredis.domain"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
