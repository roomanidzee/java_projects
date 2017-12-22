package com.romanidze.restregistrationserver.repositories;

import com.romanidze.restregistrationserver.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 16.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface PersonRepository extends JpaRepository<Person, Long>{
}
