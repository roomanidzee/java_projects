package ru.itis.romanov_andrey.perpenanto.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.ReservationDAOInterface;
import ru.itis.romanov_andrey.perpenanto.models.Reservation;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.ReservationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;
import ru.itis.romanov_andrey.perpenanto.utils.StreamCompareAttributes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationServiceImpl implements ReservationServiceInterface{

    @Autowired
    private ReservationDAOInterface reservationDAO;

    @Override
    public List<Reservation> getReservations() {
        return null;
    }

    @Override
    public List<Reservation> getReservationsByCookie(String cookieValue) {

        List<Reservation> currentReservations = this.reservationDAO.findAll();
        List<Reservation> sortedReservations = new ArrayList<>();

        int size = 3;

        Function<Reservation, String> zero = (Reservation r) -> String.valueOf(r.getId());
        Function<Reservation, String> first = Reservation::getStatus;
        Function<Reservation, String> second = (Reservation r) -> String.valueOf(r.getCreatedAt());

        List<Function<Reservation, String>> functions = Arrays.asList(zero, first, second);
        List<String> indexes = Arrays.asList("0", "1", "2");

        Map<String, Function<Reservation, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<Reservation> compareAttr = new StreamCompareAttributes<>();

        sortedReservations.addAll(compareAttr.sortList(currentReservations, functionMap, cookieValue));

        return sortedReservations;

    }

    @Override
    public void saveOrUpdate(Reservation reservation) {
        this.reservationDAO.save(reservation);
    }

    @Override
    public void delete(Long id) {
        this.reservationDAO.delete(id);
    }
}
