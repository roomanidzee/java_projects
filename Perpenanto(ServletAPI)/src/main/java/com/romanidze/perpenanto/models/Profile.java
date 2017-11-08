package com.romanidze.perpenanto.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Profile {

    private Long id;
    private Long userId;
    private List<AddressToUser> addressToUsers;
    private String personName;
    private String personSurname;


    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();

        List<Long> addressIDs = this.addressToUsers.stream()
                                                 .map(AddressToUser::getId)
                                                 .collect(Collectors.toList());

        sb.append("Profile{")
          .append("id = ").append(this.getId())
          .append(", userID = ").append(this.getUserId())
          .append(", addressToUsers = ").append(addressIDs)
          .append(", personName = ").append(this.getPersonName())
          .append(", personSurname = ").append(this.getPersonSurname())
          .append("}");


        return sb.toString();

    }

}
