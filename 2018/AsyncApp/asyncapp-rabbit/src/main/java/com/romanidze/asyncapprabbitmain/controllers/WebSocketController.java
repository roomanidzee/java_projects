package com.romanidze.asyncapprabbitmain.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.romanidze.asyncapprabbitmain.services.interfaces.CatImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * 16.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class WebSocketController {

    private final CatImageService catImageService;

    @Autowired
    public WebSocketController(CatImageService catImageService) {
        this.catImageService = catImageService;
    }

    @MessageMapping("/search_cat_for_user/{username}")
    public void searchCat(@DestinationVariable("username") String username) throws JsonProcessingException {
        this.catImageService.getImageOfCat(username);
    }
}
