package com.romanidze.perpenanto.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ReservationToUser {

    private Long id;
    private User user;
    private List<Reservation> userReservations;

    @Override
    public String toString() {

        List<Long> userReservationsIDs = this.userReservations.stream()
                                                              .map(Reservation::getId)
                                                              .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();

        sb.append("UserInfo{")
          .append("id = ").append(this.getId())
          .append(", userID = ").append(this.user.getId())
          .append(", userReservationsIDs = ").append(userReservationsIDs)
          .append("}");

        return sb.toString();

    }

}
