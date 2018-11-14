package com.romanidze.reactivecontractsredis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.reactivecontractsredis.domain.Contract;

import com.romanidze.reactivecontractsredis.messaging.subscriber.ContractInfoSubscriberImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class RedisConfig {

    private final ObjectMapper objectMapper;
    private final ReactiveRedisConnectionFactory factory;

    @Autowired
    public RedisConfig(ObjectMapper objectMapper, ReactiveRedisConnectionFactory factory) {
        this.objectMapper = objectMapper;
        this.factory = factory;
    }

    @Bean
    public ReactiveRedisOperations<String, Contract> redisOperations(ReactiveRedisConnectionFactory factory){

        Jackson2JsonRedisSerializer<Contract> serializer = new Jackson2JsonRedisSerializer<>(Contract.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Contract> builder =
                                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Contract> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);

    }

    @Bean
    public ChannelTopic channelTopic(){
        return new ChannelTopic("contract-messaging");
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(){
        return new MessageListenerAdapter(new ContractInfoSubscriberImpl(redisOperations(this.factory), this.objectMapper));
    }

}
