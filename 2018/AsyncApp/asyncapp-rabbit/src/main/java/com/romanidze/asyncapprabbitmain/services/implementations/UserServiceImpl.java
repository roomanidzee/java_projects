package com.romanidze.asyncapprabbitmain.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.asyncapprabbitmain.dto.UserDTO;
import com.romanidze.asyncapprabbitmain.dto.UserRetrieveDTO;
import com.romanidze.asyncapprabbitmain.services.interfaces.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * 07.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private final String callSentence = "get-users";

    @Value("${microservices.db-service.base}")
    private String baseURL;

    @Value("${microservices.db-service.paths.register}")
    private String registerURL;

    @Value("${microservices.db-service.paths.add_image_to_user}")
    private String imageAndUserURL;

    @Value("${microservices.db-service.paths.get_users}")
    private String getUsersURL;

    @Value("${microservices.db-service.paths.get_user}")
    private String getUserURL;

    @Value("${rabbit-services.exchange.user}")
    private String userExchange;

    @Value("${rabbit-services.registration.binding}")
    private String registrationBinding;

    @Value("${rabbit-services.cat_to_user.binding}")
    private String usersBinding;

    @Autowired
    public UserServiceImpl(RestTemplate restTemplate,
                           RabbitTemplate rabbitTemplate,
                           ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserRetrieveDTO registerUser(UserDTO userDTO) {

        ResponseEntity<UserRetrieveDTO> userResponse = null;

        try {
            userResponse = this.restTemplate.postForEntity(new URI(this.baseURL + this.registerURL),
                                                           userDTO, UserRetrieveDTO.class);

        } catch (URISyntaxException e) {
            logger.error("Пользователь не был зарегистрирован. Ошибка с путём отправки запроса.");
        }

        return Objects.requireNonNull(userResponse).getBody();

    }

    @Override
    public String addImageToUser(UserRetrieveDTO userRetrieveDTO) {

        ResponseEntity<String> stringResponse = null;

        try{
            stringResponse = this.restTemplate.postForEntity(new URI(this.baseURL + this.imageAndUserURL),
                                                             userRetrieveDTO, String.class);
        } catch (URISyntaxException e){
            logger.error("Картинка не добавилась. Ошибка с путём отправки запроса.");
        }

        return Objects.requireNonNull(stringResponse).getBody();

    }

    @Override
    public List<UserRetrieveDTO> getUsers(String callSentence) {

        assert callSentence.equals(this.callSentence) : "Получен неверный запрос на отправку пользователей";

        ResponseEntity<List<UserRetrieveDTO>> usersResponse =
                this.restTemplate.exchange(this.baseURL + this.getUsersURL, HttpMethod.GET, null,
                                           new ParameterizedTypeReference<List<UserRetrieveDTO>>(){});

        return usersResponse.getBody();

    }

    @Override
    public List<UserRetrieveDTO> getUserByID(Long userID) {

        ResponseEntity<List<UserRetrieveDTO>> usersResponse =
                this.restTemplate.exchange(this.baseURL + this.getUserURL + userID, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<UserRetrieveDTO>>(){});

        return usersResponse.getBody();

    }

    @Override
    public void registerWithRabbit(UserDTO userDTO) {

        String jsonMessage = null;
        try {
            jsonMessage = this.objectMapper.writeValueAsString(userDTO);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }

        this.rabbitTemplate.convertAndSend(this.userExchange, this.registrationBinding, jsonMessage);
    }

    @Override
    public void saveCatWithRabbit(UserRetrieveDTO userDTO) {

        String jsonMessage = null;
        try {
            jsonMessage = this.objectMapper.writeValueAsString(userDTO);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }

        this.rabbitTemplate.convertAndSend(this.userExchange, this.usersBinding, jsonMessage);
    }
}
