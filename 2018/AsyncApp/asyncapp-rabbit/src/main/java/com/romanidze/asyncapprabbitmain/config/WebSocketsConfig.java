package com.romanidze.asyncapprabbitmain.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * 16.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketsConfig extends AbstractWebSocketMessageBrokerConfigurer
                                               implements ApplicationListener<BrokerAvailabilityEvent> {

    private static final Logger logger = LogManager.getLogger(WebSocketsConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){

        config.setApplicationDestinationPrefixes("/app")
              .enableSimpleBroker("/topic");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/users_with_cats").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
       String brokerStatus = event.isBrokerAvailable() ? "Да" : "Нет";
       logger.info(new StringBuilder("Брокер доступен? ").append(brokerStatus).toString());
    }


}
