package com.romanidze.asyncappdbstorage.security.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class APIValidationError extends APISubError{

    @JsonProperty("object")
    private String object;

    @JsonProperty("field")
    private String field;

    @JsonProperty("rejected_value")
    private Object rejectedValue;

    @JsonProperty("message")
    private String message;

    public APIValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

}
