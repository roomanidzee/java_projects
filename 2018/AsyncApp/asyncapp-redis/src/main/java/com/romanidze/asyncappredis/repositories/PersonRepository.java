package com.romanidze.asyncappredis.repositories;

import com.romanidze.asyncappredis.domain.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * 20.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface PersonRepository extends CrudRepository<Person, String> {

    Person findByUsername(String username);

}
