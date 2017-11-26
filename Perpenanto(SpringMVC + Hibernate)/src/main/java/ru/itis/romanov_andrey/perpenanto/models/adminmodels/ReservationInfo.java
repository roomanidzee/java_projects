package ru.itis.romanov_andrey.perpenanto.models.adminmodels;

import lombok.*;

/**
 * 11.11.2017
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
public class ReservationInfo {

    private Long userId;
    private Long userReservationId;
    private Long reservationProductId;

}
