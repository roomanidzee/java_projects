package com.romanidze.asyncapprabbitmain.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.asyncapprabbitmain.dto.CatImageDTO;
import com.romanidze.asyncapprabbitmain.services.interfaces.CatImageService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * 07.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class CatImageServiceImpl implements CatImageService {

    private static final Logger logger = LogManager.getLogger(CatImageServiceImpl.class);

    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Value("${microservices.db-service.base}")
    private String dbBaseURL;

    @Value("${microservices.cat-service.base}")
    private String catSearchBaseURL;

    @Value("${microservices.db-service.paths.add_cat_image}")
    private String addCatImageURL;

    @Value("${microservices.cat-service.paths.search}")
    private String catSearchURL;

    @Autowired
    public CatImageServiceImpl(RestTemplate restTemplate,
                               SimpMessagingTemplate messagingTemplate,
                               ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void getImageOfCat(String username) throws JsonProcessingException {


        ResponseEntity<CatImageDTO> catImageResponse =
                this.restTemplate.getForEntity(this.catSearchBaseURL + this.catSearchURL, CatImageDTO.class);

        String tempResponse = this.addCatImage(catImageResponse.getBody());

        if(!tempResponse.contains("Картинка сохранена")){
            throw new IllegalArgumentException("Картинка кота не сохранилась");
        }

        String jsonResponse = this.objectMapper.writeValueAsString(catImageResponse.getBody());

        final String formatString = "/topic/search_cat_for_user/{0}/reply";

        this.messagingTemplate.convertAndSend(MessageFormat.format(formatString, username), jsonResponse);

    }

    @Override
    public String addCatImage(CatImageDTO catImageDTO) {

        ResponseEntity<String> stringResponse = null;

        try{
            stringResponse = this.restTemplate.postForEntity(new URI(this.dbBaseURL + this.addCatImageURL),
                                                             catImageDTO, String.class);
        } catch (URISyntaxException e){
            logger.error("Картинка не добавилась. Ошибка с путём отправки запроса.");
        }

        return Objects.requireNonNull(stringResponse).getBody();

    }
}
