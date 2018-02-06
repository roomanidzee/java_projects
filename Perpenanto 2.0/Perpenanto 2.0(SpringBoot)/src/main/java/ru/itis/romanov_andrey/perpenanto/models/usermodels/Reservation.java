package ru.itis.romanov_andrey.perpenanto.models.usermodels;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;

import java.sql.Timestamp;

/**
 * 01.01.2018
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
public class Reservation {

    private Long id;
    private Timestamp createdAt;
    private ReservationState reservationState;

}
