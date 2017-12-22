package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.ReservationInfoTransferInterface;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.ReservationToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationInfo;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;
import ru.itis.romanov_andrey.perpenanto.utils.StreamCompareAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
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
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusketServiceInterface busketService;

    @Autowired
    private ReservationToUserTransferInterface reservationToUserDTO;

    @Autowired
    private ReservationInfoTransferInterface reservationInfoDTO;

    @Override
    public List<Reservation> getReservations() {
        return this.reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByCookie(String cookieValue) {

        List<Reservation> currentReservations = this.reservationRepository.findAll();
        List<Reservation> sortedReservations = new ArrayList<>();

        int size = 3;

        Function<Reservation, String> zero = (Reservation r) -> String.valueOf(r.getId());
        Function<Reservation, String> first = (Reservation r) -> r.getStatus().toString();
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
    public List<ReservationInfo> getReservationInfosByCookie(String cookieValue) {

        List<User> currentUsers = this.userRepository.findAll();
        List<ReservationInfo> tempList = new ArrayList<>();
        List<ReservationInfo> sortedList = new ArrayList<>();
        tempList.addAll(this.reservationInfoDTO.getReservationInfos(currentUsers));

        int size = 2;

        Function<ReservationInfo, String> first = (ReservationInfo ri) -> String.valueOf(ri.getUserId());
        Function<ReservationInfo, String> second = (ReservationInfo ri) -> String.valueOf(ri.getUserReservationId());

        List<Function<ReservationInfo, String>> functions = Arrays.asList(first, second);
        List<String> indexes = Arrays.asList("1", "2");

        Map<String, Function<ReservationInfo, String>> functionMap = new HashMap<>();
        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<ReservationInfo> compareAttr = new StreamCompareAttributes<>();
        sortedList.addAll(compareAttr.sortList(tempList, functionMap, cookieValue));

        return sortedList;

    }

    @Override
    public List<ReservationToUser> getReservationToUsersByCookie(String cookieValue) {

        List<User> currentUsers = this.userRepository.findAll();
        List<ReservationToUser> tempList = new ArrayList<>();
        List<ReservationToUser> sortedList = new ArrayList<>();
        tempList.addAll(this.reservationToUserDTO.getReservationsToUsers(currentUsers));

        int size = 2;

        Function<ReservationToUser, String> first = (ReservationToUser rtu) -> String.valueOf(rtu.getUserId());
        Function<ReservationToUser, String> second = (ReservationToUser rtu) -> String.valueOf(rtu.getUserReservationId());

        List<Function<ReservationToUser, String>> functions = Arrays.asList(first, second);
        List<String> indexes = Arrays.asList("1", "2");

        Map<String, Function<ReservationToUser, String>> functionMap = new HashMap<>();
        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<ReservationToUser> compareAttr = new StreamCompareAttributes<>();
        sortedList.addAll(compareAttr.sortList(tempList, functionMap, cookieValue));

        return sortedList;

    }

    @Override
    public void saveOrUpdate(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    @Override
    public void delete(Long id) {
        this.reservationRepository.delete(id);
    }

    @Override
    public Long getReservationId(Timestamp timestamp) {

        Reservation reservation = this.reservationRepository.findByCreatedAt(timestamp);

        return reservation.getId();
    }

    @Override
    public List<Product> getReservationProducts(Profile profile, Timestamp timestamp) {

        Reservation reservation = this.reservationRepository.findByCreatedAt(timestamp);
        this.busketService.payForBusket(profile, reservation);

        return reservation.getProducts();
    }

    @Override
    public Integer getReservationPrice(Timestamp timestamp) {

        Reservation reservation = this.reservationRepository.findByCreatedAt(timestamp);
        List<Product> products = reservation.getProducts();

        return products.stream()
                       .mapToInt(Product::getPrice)
                       .sum();

    }

    @Override
    public Reservation getReservationByTimestamp(Timestamp timestamp) {
        return this.reservationRepository.findByCreatedAt(timestamp);
    }

}
