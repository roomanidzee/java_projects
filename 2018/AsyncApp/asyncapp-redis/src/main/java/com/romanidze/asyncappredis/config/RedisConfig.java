package com.romanidze.asyncappredis.config;

import com.romanidze.asyncappredis.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * 20.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class RedisConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Person> redisTemplate(){

        RedisTemplate<String, Person> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(this.redisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Person.class));

        return redisTemplate;

    }

}
