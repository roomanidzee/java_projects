package com.romanidze.perpenanto.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class User {

    private Long id;
    private String emailOrUsername;
    private String password;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("User{")
          .append("id = ").append(this.getId())
          .append(", emailOrUsername = ").append(this.getEmailOrUsername())
          .append(", password = ").append(this.getPassword())
          .append("}");

        return sb.toString();

    }
}
