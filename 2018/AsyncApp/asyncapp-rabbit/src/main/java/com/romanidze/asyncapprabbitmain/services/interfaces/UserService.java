package com.romanidze.asyncapprabbitmain.services.interfaces;

import com.romanidze.asyncapprabbitmain.dto.UserDTO;
import com.romanidze.asyncapprabbitmain.dto.UserRetrieveDTO;

import java.util.List;

/**
 * 07.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface UserService {

    UserRetrieveDTO registerUser(UserDTO userDTO);
    String addImageToUser(UserRetrieveDTO userRetrieveDTO);
    List<UserRetrieveDTO> getUsers(String callSentence);
    List<UserRetrieveDTO> getUserByID(Long userID);

    void registerWithRabbit(UserDTO userDTO);
    void saveCatWithRabbit(UserRetrieveDTO userDTO);


}
