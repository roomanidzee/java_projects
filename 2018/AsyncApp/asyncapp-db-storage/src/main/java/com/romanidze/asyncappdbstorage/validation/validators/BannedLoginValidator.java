package com.romanidze.asyncappdbstorage.validation.validators;

import com.romanidze.asyncappdbstorage.dto.auth.EmptyJSONResponse;
import com.romanidze.asyncappdbstorage.dto.auth.PersonDTO;
import com.romanidze.asyncappdbstorage.validation.constraints.BannedLoginConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class BannedLoginValidator implements ConstraintValidator<BannedLoginConstraint, String> {

    private RestTemplate restTemplate;

    @Value("${microservices.redis-service.base}")
    private String baseURL;

    @Value("${microservices.redis-service.paths.get_by_username}")
    private String usernameURL;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        ResponseEntity personCheck =
                this.restTemplate.getForEntity(this.baseURL + this.usernameURL + "/" + value, PersonDTO.class);

        return Objects.equals(personCheck.getBody().getClass(), EmptyJSONResponse.class);

    }
}
