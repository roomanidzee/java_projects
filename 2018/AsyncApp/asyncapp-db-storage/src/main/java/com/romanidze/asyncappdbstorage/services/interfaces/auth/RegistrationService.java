package com.romanidze.asyncappdbstorage.services.interfaces.auth;

import com.romanidze.asyncappdbstorage.dto.auth.RegistrationDTO;
import com.romanidze.asyncappdbstorage.dto.user.UserRetrieveDTO;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface RegistrationService {

    UserRetrieveDTO registerUser(RegistrationDTO registrationDTO);

}
