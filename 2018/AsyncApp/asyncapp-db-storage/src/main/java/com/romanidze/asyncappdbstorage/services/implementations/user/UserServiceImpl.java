package com.romanidze.asyncappdbstorage.services.implementations.user;

import com.romanidze.asyncappdbstorage.domain.CatImage;
import com.romanidze.asyncappdbstorage.domain.User;
import com.romanidze.asyncappdbstorage.dto.auth.AuthToken;
import com.romanidze.asyncappdbstorage.dto.auth.RegistrationDTO;
import com.romanidze.asyncappdbstorage.dto.user.UserRetrieveDTO;
import com.romanidze.asyncappdbstorage.repositories.CatImageRepository;
import com.romanidze.asyncappdbstorage.repositories.UserRepository;
import com.romanidze.asyncappdbstorage.services.interfaces.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final CatImageRepository catImageRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(CatImageRepository catImageRepository, UserRepository userRepository) {
        this.catImageRepository = catImageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AuthToken loginUser(RegistrationDTO registrationDTO) {
        return null;
    }

    @Override
    public void saveImageToUser(UserRetrieveDTO userRetrieveDTO) {

        Optional<User> existedUser = this.userRepository.findByUsername(userRetrieveDTO.getUsername());
        Optional<CatImage> existedCatImage = this.catImageRepository.findByImageURL(userRetrieveDTO.getImageURL());

        if(existedUser.isPresent() && existedCatImage.isPresent()){

            User user = existedUser.get();
            CatImage catImage = existedCatImage.get();

            if (user.getImages() != null) {
                user.getImages().add(catImage);
            } else {
                user.setImages(Collections.singleton(catImage));
            }

        }else{
            throw new IllegalArgumentException("Нет такого пользователя!");
        }

    }

    @Override
    public List<UserRetrieveDTO> getAllUsersWithImages(){

        CompletableFuture<List<User>> usersFuture = this.userRepository.readAllBy();

        while(!usersFuture.isDone()){
           logger.info("Получаем информацию о пользователях...");
        }

        List<UserRetrieveDTO> resultList = new ArrayList<>();

        try {

            List<User> users = usersFuture.get();

            users.forEach(user -> resultList.addAll(UserRetrieveDTO.mapFromUser(user)));

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Произошла ошибка при получении пользователей: " + e);
        }

        return resultList;

    }

    @Override
    public List<UserRetrieveDTO> getUserByID(Long userId) {

        Optional<User> existedUser = this.userRepository.findById(userId);

        if(!existedUser.isPresent()){
            logger.error("Пользователь по ID: " + userId + " не найден");
            throw new IllegalArgumentException("Неверный ID");
        }else{
            User user = existedUser.get();
            return UserRetrieveDTO.mapFromUser(user);
        }

    }
}
