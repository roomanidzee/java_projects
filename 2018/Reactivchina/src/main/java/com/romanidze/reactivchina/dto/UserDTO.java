package com.romanidze.reactivchina.dto;

import com.romanidze.reactivchina.domain.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

/**
 * 09.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserDTO {

    private Long id;
    private String username;
    private String password;

    public static UserDTO mapFromUser(User user){

        return UserDTO.builder()
                      .username(user.getUsername())
                      .password(user.getPassword())
                      .build();

    }

    public static User mapFromUserDTO(UserDTO userDTO){

        return User.builder()
                   .id(userDTO.getId())
                   .username(userDTO.getUsername())
                   .password(userDTO.getPassword())
                   .build();

    }

}
