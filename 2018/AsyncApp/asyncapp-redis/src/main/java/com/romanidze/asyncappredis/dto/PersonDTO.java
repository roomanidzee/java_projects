package com.romanidze.asyncappredis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.romanidze.asyncappredis.domain.Person;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

/**
 * 20.10.2018
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
public class PersonDTO {

    private String id;
    private String username;

    @JsonProperty("token_creation_date")
    private String tokenCreationDate;

    public static PersonDTO mapFromPerson(Person person){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        return PersonDTO.builder()
                        .username(person.getUsername())
                        .tokenCreationDate(person.getTokenDateCreation().format(formatter))
                        .build();

    }

}
