package com.romanidze.asyncapprabbitmain.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 07.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.romanidze.asyncapprabbitmain.config", "com.romanidze.asyncapprabbitmain.receivers",
                "com.romanidze.asyncapprabbitmain.controllers","com.romanidze.asyncapprabbitmain.listeners",
                "com.romanidze.asyncapprabbitmain.services"})
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
