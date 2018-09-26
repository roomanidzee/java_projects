package io.vscale.perpenanto.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 30.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan({"io.vscale.perpenanto.controllers", "io.vscale.perpenanto.repositories",
                "io.vscale.perpenanto.security", "io.vscale.perpenanto.services",
                "io.vscale.perpenanto.utils", "io.vscale.perpenanto.validators"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
