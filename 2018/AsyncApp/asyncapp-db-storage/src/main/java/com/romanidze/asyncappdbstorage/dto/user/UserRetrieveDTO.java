package com.romanidze.asyncappdbstorage.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.romanidze.asyncappdbstorage.domain.CatImage;
import com.romanidze.asyncappdbstorage.domain.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 04.10.2018
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
public class UserRetrieveDTO {

    private Long id;

    private String username;

    @JsonProperty("image_url")
    private String imageURL;

    public static List<UserRetrieveDTO> mapFromUser(User user){

        Set<CatImage> tempSet = user.getImages();


        return tempSet.stream()
                      .map(image ->
                              UserRetrieveDTO.builder()
                                             .id(user.getId())
                                             .username(user.getUsername())
                                             .imageURL(image.getImageURL())
                                             .build()
                      ).collect(Collectors.toList());

    }

}
