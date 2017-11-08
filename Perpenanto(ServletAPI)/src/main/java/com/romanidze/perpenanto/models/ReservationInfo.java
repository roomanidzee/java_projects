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
public class ReservationInfo {

    private Long id;
    private Profile userProfile;
    private Reservation userReservation;
    private List<Product> reservationProducts;

    @Override
    public String toString() {

        List<Long> userReservationsIDs = this.reservationProducts.stream()
                                             .map(Product::getId)
                                             .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();

        sb.append("ReservationInfo{")
          .append("id = ").append(this.getId())
          .append(", userProfileID = ").append(this.userProfile.getId())
          .append(", userReservationID = ").append(this.userReservation.getId())
          .append(", reservationProductsIDs = ").append(userReservationsIDs)
          .append("}");


        return sb.toString();
    }
}
