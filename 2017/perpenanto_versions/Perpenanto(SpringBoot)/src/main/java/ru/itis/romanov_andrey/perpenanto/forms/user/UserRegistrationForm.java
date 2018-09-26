package ru.itis.romanov_andrey.perpenanto.forms.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

/**
 * 12.11.2017
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
public class UserRegistrationForm {

    private String email;
    private String login;
    private String password;
    private String personName;
    private String personSurname;
    private String phone;
    private String country;
    private Integer postalCode;
    private String city;
    private String street;
    private Integer homeNumber;

}
