package com.romanidze.dockerappboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 23.09.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
