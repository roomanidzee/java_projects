package io.vscale.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.admin.ReservationForm;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.security.states.ReservationState;
import io.vscale.perpenanto.services.interfaces.admin.ReservationAdminService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationAdminServiceImpl implements ReservationAdminService{

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void addReservation(ReservationForm reservationForm) {

        ReservationState[] reservationStates = ReservationState.values();

        Optional<ReservationState> reservationState =
                Arrays.stream(reservationStates)
                      .filter(reservationState1 -> reservationState1.toString().equals(reservationForm.getStatus()))
                      .findFirst();

        if(!reservationState.isPresent()){
            throw new NullPointerException("reservation status not found!");
        }

        Reservation reservation = Reservation.builder()
                                             .createdAt(Timestamp.valueOf(reservationForm.getCreatedAt()))
                                             .reservationState(reservationState.get())
                                             .build();
        this.reservationRepository.save(reservation);

    }

    @Override
    public void updateReservation(ReservationForm reservationForm) {

        ReservationState[] reservationStates = ReservationState.values();

        Optional<ReservationState> reservationState =
                Arrays.stream(reservationStates)
                        .filter(reservationState1 -> reservationState1.toString().equals(reservationForm.getStatus()))
                        .findFirst();

        if(!reservationState.isPresent()){
            throw new NullPointerException("reservation status not found!");
        }

        Reservation reservation = Reservation.builder()
                                             .createdAt(Timestamp.valueOf(reservationForm.getCreatedAt()))
                                             .reservationState(reservationState.get())
                                             .build();
        this.reservationRepository.update(reservation);

    }

    @Override
    public void deleteReservation(Long id) {
        this.reservationRepository.delete(id);
    }
}
