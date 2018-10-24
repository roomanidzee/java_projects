package com.romanidze.asyncapprabbitmain.receivers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.romanidze.asyncapprabbitmain.dto.UserRetrieveDTO;
import com.romanidze.asyncapprabbitmain.services.interfaces.CatImageService;
import com.romanidze.asyncapprabbitmain.services.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class UserReceiver {

    private static final Logger logger = LogManager.getLogger(UserReceiver.class);

    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Autowired
    public UserReceiver(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @RabbitListener(queues = "${rabbit-services.cat_to_user.queue_name}")
    public void addCatToUser(String jsonMessage){

        logger.info("Получен JSON - объект: " + jsonMessage);

        try{

            UserRetrieveDTO userWithImageDTO = this.objectMapper.readValue(jsonMessage, UserRetrieveDTO.class);

            String addingImageResponse = this.userService.addImageToUser(userWithImageDTO);

            if(!addingImageResponse.contains("Картинка сохранена")){
                throw new IOException("Картинка кота не была сохранена");
            }

            logger.info("Результат: " + addingImageResponse);

        }catch (IOException e) {
            logger.error(new IllegalArgumentException(e));
        }

    }

}
