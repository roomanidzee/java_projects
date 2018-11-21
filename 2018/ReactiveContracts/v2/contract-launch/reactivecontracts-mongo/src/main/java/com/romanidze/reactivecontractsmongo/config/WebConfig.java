package com.romanidze.reactivecontractsmongo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class WebConfig {

    @Value("${tarantool-service.url}")
    private String tarantoolServiceURL;

    @Bean
    public WebClient webClient(){

        return WebClient.builder()
                        .baseUrl(this.tarantoolServiceURL)
                        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .build();

    }

}
