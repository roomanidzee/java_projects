package com.romanidze.asyncapprabbitmain.receivers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.asyncapprabbitmain.dto.CatImageDTO;
import com.romanidze.asyncapprabbitmain.dto.UserDTO;
import com.romanidze.asyncapprabbitmain.dto.UserRetrieveDTO;
import com.romanidze.asyncapprabbitmain.services.interfaces.CatImageService;
import com.romanidze.asyncapprabbitmain.services.interfaces.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 16.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class UserRegistrationReceiver {

    private static final Logger logger = LogManager.getLogger(UserRegistrationReceiver.class);

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserRegistrationReceiver(UserService userService,
                                    ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbit-services.registration.queue_name}")
    public void processUserRegistration(String jsonMessage){

        logger.info("Получен JSON - объект: " + jsonMessage);

        try{

            UserDTO userDTO = this.objectMapper.readValue(jsonMessage, UserDTO.class);

            UserRetrieveDTO userRetrieveDTO = this.userService.registerUser(userDTO);

            logger.info("Зарегистрированный пользователь: " + userRetrieveDTO.toString());

        }catch (IOException e) {
            logger.error(new IllegalArgumentException(e));
        }

    }

}
