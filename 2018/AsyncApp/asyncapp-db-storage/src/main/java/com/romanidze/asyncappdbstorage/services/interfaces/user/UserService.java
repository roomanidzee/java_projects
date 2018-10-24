package com.romanidze.asyncappdbstorage.services.interfaces.user;

import com.romanidze.asyncappdbstorage.dto.auth.AuthToken;
import com.romanidze.asyncappdbstorage.dto.auth.RegistrationDTO;
import com.romanidze.asyncappdbstorage.dto.user.UserRetrieveDTO;

import java.util.List;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface UserService {


    AuthToken loginUser(RegistrationDTO registrationDTO);
    void saveImageToUser(UserRetrieveDTO userRetrieveDTO);

    List<UserRetrieveDTO> getAllUsersWithImages();
    List<UserRetrieveDTO> getUserByID(Long userId);

}
