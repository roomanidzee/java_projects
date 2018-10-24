package com.romanidze.asyncappdbstorage.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.romanidze.asyncappdbstorage.domain.Token;
import com.romanidze.asyncappdbstorage.utils.LowerCaseClassNameResolver;
import com.romanidze.asyncappdbstorage.utils.RolesFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

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
@EqualsAndHashCode
@ToString
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM,
              property = "creationDate", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class AuthToken {

    private String value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    @JsonProperty("creation_date")
    private LocalDateTime creationDate;

    private String roles;

    public static AuthToken mapFromToken(Token authToken){

        String roles = RolesFormatter.getRolesString(authToken.getUser());

        return AuthToken.builder()
                        .value(authToken.getValue())
                        .creationDate(authToken.getCreationDate())
                        .roles(roles)
                        .build();

    }


}
