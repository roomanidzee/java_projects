package com.romanidze.restregistrationserver.services.implementations;

import com.romanidze.restregistrationserver.models.Person;
import com.romanidze.restregistrationserver.repositories.PersonRepository;
import com.romanidze.restregistrationserver.services.interfaces.PersonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 16.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class PersonServiceImpl implements PersonServiceInterface{

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void addPerson(Person person) {
         this.personRepository.save(person);
    }

    @Override
    public Person findLastPerson() {

        List<Person> persons = this.personRepository.findAll();
        return persons.get(persons.size() - 1);

    }
}
