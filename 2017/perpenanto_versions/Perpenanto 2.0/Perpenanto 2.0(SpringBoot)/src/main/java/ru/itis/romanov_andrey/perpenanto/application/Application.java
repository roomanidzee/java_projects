package ru.itis.romanov_andrey.perpenanto.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 30.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan("ru.itis.romanov_andrey.perpenanto")
@EntityScan(basePackages = "ru.itis.romanov_andrey.perpenanto.models.usermodels")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
