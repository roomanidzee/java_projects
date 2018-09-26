package io.vscale.perpenanto.services.implementations.user;

import io.vscale.perpenanto.dto.implementations.ReservationToUserDTO;
import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.transfermodels.ReservationToUserTransfer;
import io.vscale.perpenanto.models.usermodels.ReservationToUser;
import io.vscale.perpenanto.repositories.interfaces.ReservationToUserRepository;
import io.vscale.perpenanto.services.interfaces.user.ReservationToUserService;
import io.vscale.perpenanto.utils.userutils.CompareAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class ReservationToUserServiceImpl implements ReservationToUserService {

    private ReservationToUserRepository reservationToUserRepository;

    @Autowired
    public ReservationToUserServiceImpl(ReservationToUserRepository reservationToUserRepository) {
        this.reservationToUserRepository = reservationToUserRepository;
    }

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

        return new ArrayList<>(this.compareAttributes.sortList(currentReservationsToUsers, functionMap, cookieValue));

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
