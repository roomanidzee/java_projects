package com.romanidze.asyncappredis.controllers;

import com.romanidze.asyncappredis.dto.PersonDTO;
import com.romanidze.asyncappredis.services.interfaces.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 20.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonDTO>> retrieveAllPersons(){
        return ResponseEntity.ok(this.personService.getAllPersons());
    }

    @GetMapping("/{username}")
    public ResponseEntity<PersonDTO> retrieveByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(this.personService.getPersonByUsername(username));
    }

    @PostMapping("/add_person")
    public ResponseEntity<PersonDTO> addPerson(@RequestBody PersonDTO personDTO){
        return ResponseEntity.ok(this.personService.savePerson(personDTO));
    }

}
