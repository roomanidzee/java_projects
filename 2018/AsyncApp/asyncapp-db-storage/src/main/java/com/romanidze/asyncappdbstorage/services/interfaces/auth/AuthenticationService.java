package com.romanidze.asyncappdbstorage.services.interfaces.auth;

import com.romanidze.asyncappdbstorage.dto.auth.AuthToken;
import com.romanidze.asyncappdbstorage.dto.auth.LoginDTO;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface AuthenticationService {

    AuthToken loginUser(LoginDTO loginDTO);

}
