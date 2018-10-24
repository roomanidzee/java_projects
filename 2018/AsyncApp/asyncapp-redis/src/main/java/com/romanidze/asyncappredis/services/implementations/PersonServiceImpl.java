package com.romanidze.asyncappredis.services.implementations;

import com.romanidze.asyncappredis.domain.Person;
import com.romanidze.asyncappredis.dto.PersonDTO;
import com.romanidze.asyncappredis.repositories.PersonRepository;
import com.romanidze.asyncappredis.services.interfaces.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 20.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDTO> getAllPersons() {

        Iterable<Person> personIterable = this.personRepository.findAll();

        List<Person> persons = new ArrayList<>();
        personIterable.forEach(persons::add);

        return persons.stream()
                      .map(PersonDTO::mapFromPerson)
                      .collect(Collectors.toList());

    }

    @Override
    public PersonDTO savePerson(PersonDTO personDTO) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        Person person = Person.builder()
                              .username(personDTO.getUsername())
                              .tokenDateCreation(LocalDateTime.parse(personDTO.getTokenCreationDate(), formatter))
                              .build();

        this.personRepository.save(person);

        return PersonDTO.builder()
                        .id(person.getId())
                        .username(person.getUsername())
                        .tokenCreationDate(person.getTokenDateCreation().format(formatter))
                        .build();

    }

    @Override
    public PersonDTO getPersonByUsername(String username) {

        Person person = this.personRepository.findByUsername(username);

        return PersonDTO.mapFromPerson(person);

    }
}
