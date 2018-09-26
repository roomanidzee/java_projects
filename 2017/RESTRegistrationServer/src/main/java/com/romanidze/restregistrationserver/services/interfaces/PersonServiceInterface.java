package com.romanidze.restregistrationserver.services.interfaces;

import com.romanidze.restregistrationserver.models.Person;

/**
 * 16.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface PersonServiceInterface {

    void addPerson(Person person);
    Person findLastPerson();

}
