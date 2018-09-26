package ru.itis.romanov_andrey.perpenanto.models.adminmodels;

import lombok.*;

/**
 * 11.11.2017
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
public class AddressToUser {

    private Long userId;
    private String country;
    private Integer postalCode;
    private String city;
    private String street;
    private Integer homeNumber;

}
