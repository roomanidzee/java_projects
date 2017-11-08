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
public class TempReservationInfo {

    private Long id;
    private Long userProfileId;
    private Long userReservationId;
    private Long reservationProductId;

}
