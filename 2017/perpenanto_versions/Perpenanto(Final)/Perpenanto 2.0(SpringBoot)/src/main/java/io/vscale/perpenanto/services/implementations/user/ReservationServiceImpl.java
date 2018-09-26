package io.vscale.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.services.interfaces.user.ReservationService;
import io.vscale.perpenanto.utils.userutils.CompareAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    private CompareAttributes<Reservation> compareAttributes = (oldList, functionMap, sortType) -> {

        List<Reservation> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                List<Reservation> reservations = this.reservationRepository.findAll();
                resultList.addAll(reservations);
                break;

            case "id":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("id")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "created_at":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("created_at")))
                                    .collect(Collectors.toList());
                break;

            case "status":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("status")))
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };



    @Override
    public List<Reservation> getReservations() {
        return this.reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByCookie(String cookieValue) {

        List<Reservation> currentReservations = this.reservationRepository.findAll();

        int size = 3;

        Function<Reservation, String> idFunction = reservation -> String.valueOf(reservation.getId());
        Function<Reservation, String> createdAtFunction = reservation -> String.valueOf(reservation.getCreatedAt());
        Function<Reservation, String> statusFunction = reservation -> reservation.getReservationState().toString();

        List<Function<Reservation, String>> functions = Arrays.asList(idFunction, createdAtFunction, statusFunction);
        List<String> sortTypes = Arrays.asList("id", "created_at", "status");

        Map<String, Function<Reservation, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEach(i -> {
            String sortType = sortTypes.get(i);
            Function<Reservation, String> function = functions.get(i);
            functionMap.put(sortType, function);
        });

        return new ArrayList<>(this.compareAttributes.sortList(currentReservations, functionMap, cookieValue));

    }

    @Override
    public void saveReservation(Reservation reservation) {
       this.reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Reservation reservation) {
       this.reservationRepository.update(reservation);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
       this.reservationRepository.delete(reservation.getId());
    }

    @Override
    public Reservation getReservationByTimestamp(Timestamp timestamp) {
        return this.reservationRepository.findByCreatedAt(timestamp);
    }
}
