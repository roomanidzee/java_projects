package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.ReservationToUserDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ReservationToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ReservationToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationToUserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationToUserService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

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
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationToUserServiceImpl implements ReservationToUserService{

    @Autowired
    private ReservationToUserRepository reservationToUserRepository;

    private CompareAttributes<ReservationToUserTransfer> compareAttributes = (oldList, functionMap, sortType) -> {

        List<ReservationToUserTransfer> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                EntityDTOInterface<ReservationToUserTransfer, ReservationToUser> entityDTO = new ReservationToUserDTO();
                resultList.addAll(entityDTO.convert(this.reservationToUserRepository.findAll()));
                break;

            case "user":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("user")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "order":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("order")).reversed())
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<ReservationToUserTransfer> getReservationsToUsers() {

        EntityDTOInterface<ReservationToUserTransfer, ReservationToUser> entityDTO = new ReservationToUserDTO();
        return entityDTO.convert(this.reservationToUserRepository.findAll());

    }

    @Override
    public List<ReservationToUserTransfer> getReservationsToUsersByCookie(String cookieValue) {

        EntityDTOInterface<ReservationToUserTransfer, ReservationToUser> entityDTO = new ReservationToUserDTO();
        List<ReservationToUserTransfer> currentReservationsToUsers =
                                                          entityDTO.convert(this.reservationToUserRepository.findAll());
        List<ReservationToUserTransfer> sortedReservationsToUsers = new ArrayList<>();

        int size = 2;

        Function<ReservationToUserTransfer, String> userFunction =
                                    reservationToUserTransfer -> String.valueOf(reservationToUserTransfer.getUserId());
        Function<ReservationToUserTransfer, String> orderFunction =
                             reservationToUserTransfer -> String.valueOf(reservationToUserTransfer.getReservationId());

        List<Function<ReservationToUserTransfer, String>> functions = Arrays.asList(userFunction, orderFunction);
        List<String> sortTypes = Arrays.asList("user", "order");

        Map<String, Function<ReservationToUserTransfer, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEach(i ->{

            String sortType = sortTypes.get(i);
            Function<ReservationToUserTransfer, String> function = functions.get(i);
            functionMap.put(sortType, function);

        });

        sortedReservationsToUsers.addAll(
                this.compareAttributes.sortList(currentReservationsToUsers, functionMap, cookieValue)
        );

        return sortedReservationsToUsers;

    }

    @Override
    public void saveReservationToUser(ReservationToUser reservationToUser) {
        this.reservationToUserRepository.save(reservationToUser);
    }

    @Override
    public void updateReservationToUser(ReservationToUser reservationToUser) {
        this.reservationToUserRepository.update(reservationToUser);
    }

    @Override
    public void deleteReservationToUser(Long userId, Long orderId) {
        this.reservationToUserRepository.delete(userId, orderId);
    }
}
