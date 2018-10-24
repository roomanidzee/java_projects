package com.romanidze.asyncappdbstorage.utils;

import com.romanidze.asyncappdbstorage.domain.RoleType;
import com.romanidze.asyncappdbstorage.domain.User;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public class RolesFormatter {

    public static String getRolesString(User user){

        Set<RoleType> userRoles = user.getRoles();

        String rolesString = userRoles.stream()
                                      .map(role -> role.getRole().toString() + ",")
                                      .collect(Collectors.joining());

        return rolesString.substring(0, rolesString.length() - 1);

    }

}
