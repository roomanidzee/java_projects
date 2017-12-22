package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.repositories.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationAdminServiceInterface;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationAdminServiceImpl implements ReservationAdminServiceInterface{

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void addReservation(ReservationForm reservationForm) {

        ReservationState[] reservationStatesTemp = ReservationState.values();

        ReservationState state = Arrays.stream(reservationStatesTemp)
                                       .filter(reservationState -> reservationForm.getStatus()
                                                                                  .equals(reservationState.toString()))
                                       .findFirst()
                                       .orElseThrow(() -> new NullPointerException("No such Reservation status!"));

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(reservationForm.getCreatedAt(), dateFormat);
        Timestamp createdAt = Timestamp.valueOf(localDateTime);

        Reservation reservation = Reservation.builder()
                                             .createdAt(createdAt)
                                             .status(state)
                                             .build();
        this.reservationRepository.save(reservation);

    }

    @Override
    public void updateReservation(ReservationForm reservationForm) {

        ReservationState[] reservationStatesTemp = ReservationState.values();

        ReservationState state = Arrays.stream(reservationStatesTemp)
                                       .filter(reservationState -> reservationForm.getStatus()
                                                                                  .equals(reservationState.toString()))
                                       .findFirst()
                                       .orElseThrow(() -> new NullPointerException("No such Reservation status!"));

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(reservationForm.getCreatedAt(), dateFormat);
        Timestamp createdAt = Timestamp.valueOf(localDateTime);

        Reservation reservation = this.reservationRepository.findOne(reservationForm.getId());

        reservation.setCreatedAt(createdAt);
        reservation.setStatus(state);

        this.reservationRepository.save(reservation);

    }

    @Override
    public void deleteReservation(Long id) {
        this.reservationRepository.delete(id);
    }
}
