package com.romanidze.siteinfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 04.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisOperations<String, String> redisOperations(ReactiveRedisConnectionFactory factory){

        Jackson2JsonRedisSerializer<String> serializer = new Jackson2JsonRedisSerializer<>(String.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, String> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);

    }

}
