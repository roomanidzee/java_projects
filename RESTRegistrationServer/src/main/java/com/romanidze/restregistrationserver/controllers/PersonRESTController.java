package com.romanidze.restregistrationserver.controllers;

import com.romanidze.restregistrationserver.models.Person;
import com.romanidze.restregistrationserver.services.interfaces.PersonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 16.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
public class PersonRESTController {

    @Autowired
    private PersonServiceInterface personService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody ResponseEntity<Void> createPerson(Person person){

        this.personService.addPerson(person);
        String redirectURL = "http://127.0.0.1:8081/register";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURL));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/get_profile")
    public ResponseEntity<Person> getPersonInfo(){

        return new ResponseEntity<>(this.personService.findLastPerson(), HttpStatus.OK);

    }

}
