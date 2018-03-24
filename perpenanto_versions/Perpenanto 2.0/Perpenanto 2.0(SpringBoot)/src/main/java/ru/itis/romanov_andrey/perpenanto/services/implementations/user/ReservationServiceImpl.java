package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.BusketRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

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

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BusketRepository busketRepository;

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
        List<Reservation> sortedReservations = new ArrayList<>();

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

        sortedReservations.addAll(this.compareAttributes.sortList(currentReservations, functionMap, cookieValue));
        return sortedReservations;

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
