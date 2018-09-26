package com.romanidze.dockerappboot.receivers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.dockerappboot.dto.UserDTO;
import com.romanidze.dockerappboot.services.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 23.09.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

@Component
public class EmailReceiver {

    private static final Logger logger = LogManager.getLogger(EmailReceiver.class);
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmailReceiver(UserService userService, ObjectMapper objectMapper){
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "demo-queue")
    public void process(byte[] messageAsBytes){

        String jsonMessage = new String(messageAsBytes);
        logger.info("Получен JSON - объект: " + jsonMessage);

        try{

            UserDTO userDTO = this.objectMapper.readValue(jsonMessage, UserDTO.class);
            logger.info("Получен пользователь: " + userDTO.toString());

            this.userService.saveUser(userDTO);

            List<UserDTO> users = this.userService.getAllUsers();

            users.forEach(user -> logger.info("Пользователь из базы: " + user.toString()));

        }catch (IOException e) {
            logger.error(new IllegalArgumentException(e));
        }

    }

}
