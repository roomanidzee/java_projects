package com.romanidze.asyncappredis.services.interfaces;

import com.romanidze.asyncappredis.dto.PersonDTO;

import java.util.List;

/**
 * 20.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface PersonService {

    List<PersonDTO> getAllPersons();
    PersonDTO savePerson(PersonDTO personDTO);
    PersonDTO getPersonByUsername(String username);

}
