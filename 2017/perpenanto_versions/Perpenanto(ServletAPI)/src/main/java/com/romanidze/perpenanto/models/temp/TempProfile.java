package com.romanidze.perpenanto.models.temp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class TempProfile {

    private Long id;
    private String personName;
    private String personSurname;
    private Long userId;
    private Long addressId;

}
